package br.com.zup.matheuscarv69.chavePix.controllers.remover.request

import br.com.zup.matheuscarv69.RemoverChavePixRequest
import br.com.zup.matheuscarv69.core.validacao.ValidUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank

@Introspected
data class RemoverChaveRequest(
    @field:NotBlank @ValidUUID val clienteId: UUID,
    @field:NotBlank @ValidUUID val pixId: UUID
) {

    fun toModelGrpc() = RemoverChavePixRequest
        .newBuilder()
        .setClienteId(clienteId.toString())
        .setPixId(pixId.toString())
        .build()

}