package br.com.zup.matheuscarv69.chavePix.controllers.detalhar.response

import br.com.zup.matheuscarv69.DetalharChavePixResponse
import br.com.zup.matheuscarv69.TipoDeContaGrpc
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DetalhaChaveResponse(grpcResponse: DetalharChavePixResponse) {
    val pixId = grpcResponse.pixId
    val tipoDeChave = grpcResponse.chavePix.tipoDeChave
    val chave = grpcResponse.chavePix.chave

    val criadaEm = grpcResponse.chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoDeConta = when (grpcResponse.chavePix.conta.tipo) {
        TipoDeContaGrpc.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TipoDeContaGrpc.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "NAO_RECONHECIDA"
    }

    val conta = mapOf(
        Pair("tipo", tipoDeConta),
        Pair("instituicao", grpcResponse.chavePix.conta.instituicao),
        Pair("nomeDoTitular", grpcResponse.chavePix.conta.nomeDoTitular),
        Pair("cpfDoTitular", grpcResponse.chavePix.conta.cpfDoTitular),
        Pair("agencia", grpcResponse.chavePix.conta.agencia),
        Pair("numero", grpcResponse.chavePix.conta.numeroDaConta)
    )
}
