package br.com.zup.matheuscarv69.chavePix.controllers.lista

import br.com.zup.matheuscarv69.*
import br.com.zup.matheuscarv69.grpcClients.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavesControllerTest {

    @field:Inject
    lateinit var listaGrpcClient: KeyManagerListaChavesServiceGrpc.KeyManagerListaChavesServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    companion object {
        val CLIENTE_ID = UUID.randomUUID().toString()
        val PIX_ID = UUID.randomUUID().toString()
        val CHAVE_EMAIL = "teste@teste.com.br"
        val CHAVE_CELULAR = "+5511912345678"
        val CONTA_CORRENTE = TipoDeContaGrpc.CONTA_CORRENTE
        val TIPO_DE_CHAVE_EMAIL = TipoDeChaveGrpc.EMAIL
        val TIPO_DE_CHAVE_CELULAR = TipoDeChaveGrpc.CELULAR
        val INSTITUICAO = "Itau"
        val TITULAR = "Woody"
        val DOCUMENTO_DO_TITULAR = "34597563067"
        val AGENCIA = "0001"
        val NUMERO_DA_CONTA = "1010-1"
        val CHAVE_CRIADA_EM = LocalDateTime.now()
    }

    @Test
    internal fun `Deve listar todas as chaves pix de um cliente`() {

        // cenario
        val respostaGrpc = listaChaveResponse(CLIENTE_ID)

        // acao
        Mockito.`when`(listaGrpcClient.lista(Mockito.any())).thenReturn(respostaGrpc)

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$CLIENTE_ID/pix/")
        val response = httpClient.toBlocking().exchange(request, List::class.java)

        // validacao
        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            assertNotNull(body())
            assertEquals(body()!!.size, 2)
        }
    }

    private fun listaChaveResponse(clienteId: String): ListaChavesResponse {
        val chaveEmail = ListaChavesResponse.ChaveResponse.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipoDeChave(TIPO_DE_CHAVE_EMAIL)
            .setChave(CHAVE_EMAIL)
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(CHAVE_CRIADA_EM.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveCelular = ListaChavesResponse.ChaveResponse.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipoDeChave(TIPO_DE_CHAVE_CELULAR)
            .setChave(CHAVE_CELULAR)
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(CHAVE_CRIADA_EM.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        return ListaChavesResponse.newBuilder()
            .setClienteId(clienteId)
            .addAllChaves(listOf(chaveEmail, chaveCelular))
            .build()
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(KeyManagerListaChavesServiceGrpc.KeyManagerListaChavesServiceBlockingStub::class.java)

    }

}