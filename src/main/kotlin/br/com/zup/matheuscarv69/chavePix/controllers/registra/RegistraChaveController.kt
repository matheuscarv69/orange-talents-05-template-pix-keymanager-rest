package br.com.zup.matheuscarv69.entities.chavePix.controllers

import br.com.zup.matheuscarv69.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.matheuscarv69.chavePix.controllers.registra.request.NovaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes")
class RegistraChaveController(
    @Inject val registraClient: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub
) {

    @Post("/{clienteId}/pix")
    fun registra(
        @PathVariable clienteId: UUID,
        @Body @Valid req: NovaChavePixRequest
    ): HttpResponse<Any> {

        val grpcRequest = req.toModelGrpc(clienteId = clienteId)

        val grpcResponse = registraClient.registrar(grpcRequest)

        return HttpResponse.created(generateLocationHeader(clienteId = clienteId, pixId = grpcResponse.pixId))
    }

    private fun generateLocationHeader(clienteId: UUID, pixId: String) = HttpResponse
        .uri("/api/clientes/$clienteId/pix/$pixId")

}