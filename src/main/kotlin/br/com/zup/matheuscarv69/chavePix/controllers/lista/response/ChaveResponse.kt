package br.com.zup.matheuscarv69.chavePix.controllers.lista.response

import br.com.zup.matheuscarv69.ListaChavesResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChaveResponse(chavePix: ListaChavesResponse.ChaveResponse) {
    val pixId = chavePix.pixId
    val chave = chavePix.chave
    val tipoDeChave = chavePix.tipoDeChave
    val tipoDeConta = chavePix.tipoDeConta
    val criadaEm = chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}
