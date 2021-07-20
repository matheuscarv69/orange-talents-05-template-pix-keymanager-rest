package br.com.zup.matheuscarv69.chavePix.controllers.detalhar

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
internal class DetalharChaveControllerTest {

    @field:Inject
    lateinit var detalharGrpcClient: KeyManagerDetalhaChaveServiceGrpc.KeyManagerDetalhaChaveServiceBlockingStub

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
    internal fun `Deve detalhar chave pix existente`() {

        // cenario


        // acao
        Mockito.`when`(detalharGrpcClient.detalhar(detalharChaveRequest()))
            .thenReturn(detalharChavePixResponse(CLIENTE_ID, PIX_ID))

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$CLIENTE_ID/pix/$PIX_ID")
        val response = httpClient.toBlocking().exchange(request, Any::class.java)

        // validacao
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())

    }

    private fun detalharChaveRequest() = DetalharChavePixRequest
        .newBuilder()
        .setPixIdEClienteId(
            DetalharChavePixRequest.ChavePixRequest
                .newBuilder()
                .setPixId(PIX_ID)
                .setClienteId(CLIENTE_ID)
                .build()
        ).setChave("")
        .build()

    private fun detalharChavePixResponse(clienteId: String, pixId: String) =
        DetalharChavePixResponse.newBuilder()
            .setPixId(pixId)
            .setClienteId(clienteId)
            .setChavePix(
                DetalharChavePixResponse.ChavePixResponse
                    .newBuilder()
                    .setTipoDeChave(TIPO_DE_CHAVE_EMAIL)
                    .setChave(CHAVE_EMAIL)
                    .setConta(
                        DetalharChavePixResponse.ContaResponse.newBuilder()
                            .setTipo(CONTA_CORRENTE)
                            .setInstituicao(INSTITUICAO)
                            .setNomeDoTitular(TITULAR)
                            .setCpfDoTitular(DOCUMENTO_DO_TITULAR)
                            .setAgencia(AGENCIA)
                            .setNumeroDaConta(NUMERO_DA_CONTA)
                            .build()
                    )
                    .setCriadaEm(CHAVE_CRIADA_EM.let {
                        val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                        Timestamp.newBuilder()
                            .setSeconds(createdAt.epochSecond)
                            .setNanos(createdAt.nano)
                            .build()
                    })
            ).build()

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(KeyManagerDetalhaChaveServiceGrpc.KeyManagerDetalhaChaveServiceBlockingStub::class.java)

    }


}