package br.com.zup.matheuscarv69.chavePix.controllers.detalhar

import br.com.zup.matheuscarv69.KeyManagerDetalhaChaveServiceGrpc
import br.com.zup.matheuscarv69.chavePix.controllers.detalhar.request.DetalhaChaveRequest
import br.com.zup.matheuscarv69.chavePix.controllers.detalhar.response.DetalhaChaveResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes")
class DetalharChaveController(
    @Inject val detalharClient: KeyManagerDetalhaChaveServiceGrpc.KeyManagerDetalhaChaveServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/{clienteId}/pix/{pixId}")
    fun detalha(@PathVariable clienteId: UUID, @PathVariable pixId: UUID): HttpResponse<Any> {

        val grpcRequest = DetalhaChaveRequest(clienteId, pixId).toModelGrpc()

        LOGGER.info("Detalhando chave pix: $pixId, do cliente: $clienteId")
        val grpcResponse = detalharClient.detalhar(grpcRequest)

        return HttpResponse.ok(DetalhaChaveResponse(grpcResponse))
    }

}