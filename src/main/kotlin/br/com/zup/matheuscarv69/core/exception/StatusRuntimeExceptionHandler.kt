package br.com.zup.matheuscarv69.core.exception

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

/**
 * Handler para pegar as exceptions (StatusRuntimeException) lancadas pelos clientes gRPC.
 *
 * Ela faz um bind entre o Status lançado e retorna um HttpResponse com o StatusCode correspondente
 * */
@Singleton
class StatusRuntimeExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<Any> {

        val statusCode = exception.status.code
        val statusDescription = exception.status.description ?: ""

        // Verifica o statusCode e retorna um Pair com o HttpStatus correspondente junto da respectiva mensagem
        val (httpStatus, message) = when (statusCode) {
            Status.NOT_FOUND.code -> Pair(HttpStatus.NOT_FOUND, statusDescription)
            Status.INVALID_ARGUMENT.code -> Pair(HttpStatus.BAD_REQUEST, "Dados da requisição estão inválidos")
            Status.ALREADY_EXISTS.code -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, statusDescription)
            // Caso seja capturado um erro que nao foi mapeado acima, eh retornado esse Internal Server Error
            else -> {
                LOGGER.error("Erro inesperado '${exception.javaClass.name}' ao processar requisição", exception)
                Pair(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Nao foi possivel completar a requisição devido ao erro: ${statusDescription} (${statusCode})"
                )
            }
        }

        // Retorna o HttpResponse com o StatusCode e a mensagem obtidos por meio do when acima
        // Uso da classe JsonError, ela converte o objeto para um formato em Json adequado.
        return HttpResponse
            .status<JsonError>(httpStatus)
            .body(JsonError(message))
    }


}