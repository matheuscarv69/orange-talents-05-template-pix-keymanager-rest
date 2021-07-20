package br.com.zup.matheuscarv69.chavePix.controllers.detalhar.request

import br.com.zup.matheuscarv69.DetalharChavePixRequest
import br.com.zup.matheuscarv69.core.validacao.ValidUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank

@Introspected
class DetalhaChaveRequest(
    @field:NotBlank @ValidUUID val clienteId: UUID,
    @field:NotBlank @ValidUUID val pixId: UUID
) {

    fun toModelGrpc() = DetalharChavePixRequest
        .newBuilder()
        .setPixIdEClienteId(
            DetalharChavePixRequest.ChavePixRequest
                .newBuilder()
                .setClienteId(clienteId.toString())
                .setPixId(pixId.toString())
                .build()
        )
        .build()

}
