package br.com.zup.ot5.compartilhado

import br.com.zup.ot5.core.exception_handler.KeyManagerGrpcExceptionHandler
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
class KeyManagerGrpcExceptionHandlerUnitTest(
    private val keyManagerGrpcExceptionHandler: KeyManagerGrpcExceptionHandler
){


    companion object{
        val REQUISICAO_QUALQUER = HttpRequest.GET<Any>("http://localhost:8080/api/v1/recurso")
    }

    @Test
    fun `Deve retornar codigo 404 (Not found) quando o status code gRPC for NOT_FOUND`(){

        // cenario
        val notFoundStatusException = StatusRuntimeException(
            Status.NOT_FOUND
        )

        // exec
        val response = keyManagerGrpcExceptionHandler.handle(
            REQUISICAO_QUALQUER,
            notFoundStatusException
        )

        // validacao
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.status)
    }

    @Test
    fun `Deve retornar codigo 400 (Bad Request) quando o status code gRPC for INVALID_ARGUMENT`(){

        // cenario
        val invalidArgumentStatusException = StatusRuntimeException(
            Status.INVALID_ARGUMENT
        )

        // exec
        val response = keyManagerGrpcExceptionHandler.handle(
            REQUISICAO_QUALQUER,
            invalidArgumentStatusException
        )

        // validacao
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.status)
    }

    @Test
    fun `Deve retornar codigo 409 (Conflict) quando o status code gRPC for ALREADY_EXISTS`(){

        // cenario
        val alreadyExistsStatusException = StatusRuntimeException(
            Status.ALREADY_EXISTS
        )

        // exec
        val response = keyManagerGrpcExceptionHandler.handle(
            REQUISICAO_QUALQUER,
            alreadyExistsStatusException
        )

        // validacao
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CONFLICT, response.status)
    }

    @Test
    fun `Deve retornar codigo 422 (Unprocessable Entity) quando o status code gRPC for FAILED_PRECONDITION`(){

        // cenario
        val failedPreconditionStatusException = StatusRuntimeException(
            Status.FAILED_PRECONDITION
        )

        // exec
        val response = keyManagerGrpcExceptionHandler.handle(
            REQUISICAO_QUALQUER,
            failedPreconditionStatusException
        )

        // validacao
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
    }

    @Test
    fun `Deve retornar codigo 500 quando o status code gRPC for diferente de ALREADY_EXISTS, FAILED_PRECONDITION, ALREADY_EXISTS, INVALID_ARGUMENT e NOT_FOUND`(){

        // cenario
        val exceptionStatusDiferente = StatusRuntimeException(
            Status.CANCELLED
        )

        // exec
        val response = keyManagerGrpcExceptionHandler.handle(
            REQUISICAO_QUALQUER,
            exceptionStatusDiferente
        )

        // validacao
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
    }




}