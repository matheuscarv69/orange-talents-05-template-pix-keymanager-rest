package br.com.zup.matheuscarv69.chavePix.controllers.remover

import br.com.zup.matheuscarv69.KeyManagerRemoverServiceGrpc
import br.com.zup.matheuscarv69.chavePix.controllers.remover.request.RemoverChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.validation.Validator

@Controller("/api/v1/clientes")
class RemoverChaveController(
    @Inject val removerClient: KeyManagerRemoverServiceGrpc.KeyManagerRemoverServiceBlockingStub,
    @Inject val validator: Validator
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/{clienteId}/pix/{pixId}")
    fun deleta(@PathVariable clienteId: UUID, @PathVariable pixId: UUID): HttpResponse<Any> {

        val grpcRequest = RemoverChaveRequest(clienteId, pixId).toModelGrpc()

        LOGGER.info("Removendo chave pix: $pixId do cliente : $clienteId")
        val grpcResponse = removerClient.remover(grpcRequest)

        return HttpResponse.ok(grpcResponse.mensagem)
    }


}