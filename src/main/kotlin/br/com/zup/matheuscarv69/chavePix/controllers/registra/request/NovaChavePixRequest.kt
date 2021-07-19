package br.com.zup.matheuscarv69.chavePix.controllers.registra.request

import br.com.zup.matheuscarv69.RegistraChavePixRequest
import br.com.zup.matheuscarv69.TipoDeChaveGrpc
import br.com.zup.matheuscarv69.TipoDeContaGrpc
import br.com.zup.matheuscarv69.core.validacao.ValidPixKey
import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
data class NovaChavePixRequest(
    @field:NotNull val tipoDeChave: TipoDeChaveRequest?,
    @field:Size(max = 77) val chave: String?,
    @field:NotNull val tipoDeConta: TipoDeContaRequest?
) {

    fun toModelGrpc(clienteId: UUID) = RegistraChavePixRequest
        .newBuilder()
        .setClienteId(clienteId.toString())
        .setTipoDeChave(tipoDeChave?.atributoGrpc ?: TipoDeChaveGrpc.TIPO_DE_CHAVE_DESCONHECIDA)
        .setChave(chave ?: "")
        .setTipoDeConta(tipoDeConta?.atributoGrpc ?: TipoDeContaGrpc.TIPO_CONTA_DESCONHECIDA)
        .build()

}

enum class TipoDeChaveRequest(val atributoGrpc: TipoDeChaveGrpc) {

    CPF(TipoDeChaveGrpc.CPF) {

        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return CPFValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }

    },

    CELULAR(TipoDeChaveGrpc.CELULAR) {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoDeChaveGrpc.EMAIL) {

        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }

            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },

    ALEATORIA(TipoDeChaveGrpc.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank() // não deve se preenchida
    };

    abstract fun valida(chave: String?): Boolean
}

enum class TipoDeContaRequest(val atributoGrpc: TipoDeContaGrpc) {

    CONTA_CORRENTE(TipoDeContaGrpc.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoDeContaGrpc.CONTA_POUPANCA)
}
