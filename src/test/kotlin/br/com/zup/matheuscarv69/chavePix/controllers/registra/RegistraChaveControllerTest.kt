package br.com.zup.matheuscarv69.chavePix.controllers.registra

import br.com.zup.matheuscarv69.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.matheuscarv69.RegistraChavePixResponse
import br.com.zup.matheuscarv69.chavePix.controllers.registra.request.NovaChavePixRequest
import br.com.zup.matheuscarv69.chavePix.controllers.registra.request.TipoDeChaveRequest
import br.com.zup.matheuscarv69.chavePix.controllers.registra.request.TipoDeContaRequest
import br.com.zup.matheuscarv69.grpcClients.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChaveControllerTest {

    @field:Inject
    lateinit var registraGrpcClient: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    companion object {
        val CLIENTE_ID = UUID.randomUUID().toString()
        val PIX_ID = UUID.randomUUID().toString()
    }

    @Test
    fun `Deve registrar uma nova chave pix`() {

        // cenario
        val httpNovaChaveRequest = novaChavePixRequest()
        val grpcRequest = httpNovaChaveRequest.toModelGrpc(clienteId = UUID.fromString(CLIENTE_ID))

        // mockando a chamada ao cliente grpc
        val grpcResponse = RegistraChavePixResponse.newBuilder().setPixId(PIX_ID).build()
        Mockito.`when`(registraGrpcClient.registrar(grpcRequest)).thenReturn(grpcResponse)

        // acao
        val httpRequest = HttpRequest.POST("/api/v1/clientes/$CLIENTE_ID/pix", httpNovaChaveRequest)
        val httpResponse =
            httpClient.toBlocking().exchange(httpRequest, NovaChavePixRequest::class.java) // param: request , bodyType

        // validacao
        with(httpResponse) {
            assertEquals(HttpStatus.CREATED.code, status.code)
            assertTrue(headers.contains("Location"))
            assertTrue(header("Location")!!.contains(PIX_ID))
        }

    }

    private fun novaChavePixRequest(): NovaChavePixRequest {
        val novaChavePixRequest = NovaChavePixRequest(
            tipoDeChave = TipoDeChaveRequest.EMAIL,
            chave = "yuri.matheus@zup.com.br",
            tipoDeConta = TipoDeContaRequest.CONTA_CORRENTE
        )
        return novaChavePixRequest
    }


    // Mockando o servico GRPC de Registrar Chave Pix

    @Factory
    @Replaces(factory = GrpcClientFactory::class) // essa anotacao diz para o micronaut substituir a factory de GrpcClient Factory, pela classe abaixo
    internal class MockitoStubFactory {

        // Mockando o servico de registrar
        @Singleton
        fun stubMock() =
            Mockito.mock(KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub::class.java)

    }


}