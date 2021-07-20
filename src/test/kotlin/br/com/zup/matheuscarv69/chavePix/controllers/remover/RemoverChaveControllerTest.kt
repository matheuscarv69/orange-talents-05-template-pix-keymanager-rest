package br.com.zup.matheuscarv69.chavePix.controllers.remover

import br.com.zup.matheuscarv69.KeyManagerRemoverServiceGrpc
import br.com.zup.matheuscarv69.RemoverChavePixResponse
import br.com.zup.matheuscarv69.chavePix.controllers.registra.RegistraChaveControllerTest
import br.com.zup.matheuscarv69.chavePix.controllers.remover.request.RemoverChaveRequest
import br.com.zup.matheuscarv69.grpcClients.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoverChaveControllerTest {

    @field:Inject
    lateinit var removerGrpcClient: KeyManagerRemoverServiceGrpc.KeyManagerRemoverServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    companion object {
        val CLIENTE_ID = UUID.randomUUID().toString()
        val PIX_ID = UUID.randomUUID().toString()
    }

    @Test
    internal fun `Deve remover uma chave pix`() {
        // cenario
        val httpRemoverChaveRequest = RemoverChaveRequest(UUID.fromString(CLIENTE_ID), UUID.fromString(PIX_ID))
        val grpcRequest = httpRemoverChaveRequest.toModelGrpc()

        // mockando chamada ao grpc
        val grpcResponse = RemoverChavePixResponse.newBuilder().setMensagem("Removido").build()
        Mockito.`when`(removerGrpcClient.remover(grpcRequest)).thenReturn(grpcResponse)

        // acao
        val httpRequest = HttpRequest.DELETE<Any>("/api/v1/clientes/$CLIENTE_ID/pix/$PIX_ID")
        val httpResponse = httpClient.toBlocking().exchange(httpRequest, RemoverChaveRequest::class.java)

        // validacao
        with(httpResponse) {
            assertEquals(HttpStatus.OK.code, status.code)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerRemoverServiceGrpc.KeyManagerRemoverServiceBlockingStub::class.java)

    }


}


