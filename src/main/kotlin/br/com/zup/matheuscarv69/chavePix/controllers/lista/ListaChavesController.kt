package br.com.zup.matheuscarv69.chavePix.controllers.lista

import br.com.zup.matheuscarv69.KeyManagerListaChavesServiceGrpc
import br.com.zup.matheuscarv69.ListaChavesRequest
import br.com.zup.matheuscarv69.chavePix.controllers.lista.response.ChaveResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes")
class ListaChavesController(
    @Inject val listaClient: KeyManagerListaChavesServiceGrpc.KeyManagerListaChavesServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/{clienteId}/pix")
    fun lista(@PathVariable clienteId: UUID): HttpResponse<Any> {

        LOGGER.info("Listando chaves do cliente: $clienteId")
        val grpcResponse = listaClient.lista(
            ListaChavesRequest
                .newBuilder()
                .setClienteId(clienteId.toString())
                .build()
        )

        val listaChaves = grpcResponse.chavesList.map { ChaveResponse(it) }

        return HttpResponse.ok(listaChaves)
    }

}