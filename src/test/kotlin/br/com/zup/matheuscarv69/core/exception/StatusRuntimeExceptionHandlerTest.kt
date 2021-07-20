package br.com.zup.matheuscarv69.core.exception

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StatusRuntimeExceptionHandlerTest {

    val requestGenerica = HttpRequest.GET<Any>("/")

    @Test
    fun `Deve retornar 404 quando a StatusException for Not_Found`() {

        // cenario
        val mensagem = "Chave nao encontrada"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))

        // acao
        val httpResponse =
            StatusRuntimeExceptionHandler().handle(request = requestGenerica, exception = notFoundException)

        // validacao
        with(httpResponse) {
            assertEquals(HttpStatus.NOT_FOUND.code, status.code)
            assertNotNull(httpResponse.body())
            assertEquals(mensagem, (httpResponse.body() as JsonError).message)
        }
    }

    @Test
    fun `Deve retornar 400 quando a StatusException for Invalid_Argument`() {

        // cenario
        val mensagem = "Dados da requisição estão inválidos"
        val badRequestException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(mensagem))

        // acao
        val httpResponse =
            StatusRuntimeExceptionHandler().handle(request = requestGenerica, exception = badRequestException)

        // validacao
        with(httpResponse) {
            assertEquals(HttpStatus.BAD_REQUEST.code, status.code)
            assertNotNull(httpResponse.body())
            assertEquals(mensagem, (httpResponse.body() as JsonError).message)
        }
    }

    @Test
    fun `Deve retornar 400 quando a StatusException for Failed_Precondition`() {

        // cenario
        val mensagem = "Já existe essa chave pix"
        val badRequestException = StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(mensagem))

        // acao
        val httpResponse =
            StatusRuntimeExceptionHandler().handle(request = requestGenerica, exception = badRequestException)

        // validacao
        with(httpResponse) {
            assertEquals(HttpStatus.BAD_REQUEST.code, status.code)
            assertNotNull(httpResponse.body())
            assertEquals(mensagem, (httpResponse.body() as JsonError).message)
        }
    }

    @Test
    fun `Deve retornar 422 quando a StatusException for Already_Exists`() {

        // cenario
        val mensagem = "Chave já existente"
        val alreadyExistsException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))

        // acao
        val httpResponse =
            StatusRuntimeExceptionHandler().handle(request = requestGenerica, exception = alreadyExistsException)

        // validacao
        with(httpResponse) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.code, status.code)
            assertNotNull(httpResponse.body())
            assertEquals(mensagem, (httpResponse.body() as JsonError).message)
        }
    }

    @Test
    fun `Deve retornar 500 quando a StatusException nao for esperada`() {

        // cenario
        val mensagem = "Deu ruim aqui viu"
        val internalServerError = StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(mensagem))

        // acao
        val httpResponse =
            StatusRuntimeExceptionHandler().handle(request = requestGenerica, exception = internalServerError)

        // validacao
        with(httpResponse) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, status.code)
            assertNotNull(httpResponse.body())
            assertTrue((httpResponse.body() as JsonError).message.contains(mensagem))
        }
    }
}