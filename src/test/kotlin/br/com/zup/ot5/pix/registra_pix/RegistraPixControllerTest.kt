package br.com.zup.ot5.pix.registra_pix

import br.com.zup.ot5.KeyManagerRegistraServiceGrpc
import br.com.zup.ot5.RegistraChavePixRequest
import br.com.zup.ot5.RegistraChavePixResponse
import br.com.zup.ot5.core.grpc.PixGrpcStubFactory
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class RegistraPixControllerTest{

    @field:Inject
    private lateinit var registraPixStub: KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub

    @field:Inject
    @field:Client("/")
    private lateinit var client: HttpClient


    companion object{
        const val ID_TITULAR_VALIDO = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        const val PIX_ID_VALIDO = "261b9877-f31a-4487-bade-a136232399a9"
        const val CPF_VALIDO = "02467781054"
    }

    @Test
    fun `Deve retornar Codigo 201 Created quando a chave pix for valida`(){

        // cenario
        val request = HttpRequest.POST(
            UriBuilder.of("/api/v1/clientes/$ID_TITULAR_VALIDO/pix").build(),
            chavePixValida()
        )

        Mockito.`when`(
            registraPixStub.registra(
                Mockito.any()
            )
        ).thenReturn(
            RegistraChavePixResponse.newBuilder()
                .setIdTitular(ID_TITULAR_VALIDO)
                .setPixId(PIX_ID_VALIDO)
                .build()
        )

        // exec
        val response = client.toBlocking().exchange(request, RegistraPixRequest::class.java)

        // validacao
        Assertions.assertEquals("/api/v1/clientes/$ID_TITULAR_VALIDO/pix/$PIX_ID_VALIDO", response.header("Location"))
        Assertions.assertNotNull(response)

    }

//    @Test
//    fun `Deve retornar Codigo 400 Bad Request quando a chave pix for invalida`(){
//
//        // cenario
//        val request = HttpRequest.POST(
//            UriBuilder.of("/api/v1/clientes/$ID_TITULAR_VALIDO/pix").build(),
//            chavePixTotalmenteInvalida()
//        )
//
//        Mockito.`when`(
//            registraPixStub.registra(
//                chavePixTotalmenteInvalida().paraGrpcRequest(ID_TITULAR_VALIDO)
//            )
//        ).thenThrow(
//            StatusRuntimeException(Status.INVALID_ARGUMENT)
//        )
//
//        // exec
//        val exception = Assertions.assertThrows(HttpClientResponseException::class.java){
//            client.toBlocking().exchange(request, RegistraPixRequest::class.java)
//        }
//
//        // validacao
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.status)
//        Assertions.assertNotNull(exception)
//
//    }
//
//    @Test
//    fun `Deve retornar Codigo 422 Unprocessable Entity quando o stub grpc retornar um codigo FAILED_PRECONDITION`(){
//
//        // cenario
//        val request = HttpRequest.POST(
//            UriBuilder.of("/api/v1/clientes/$ID_TITULAR_VALIDO/pix").build(),
//            chavePixValida()
//        )
//
//
//        Mockito.`when`(
//            registraPixStub.registra(
//                Mockito.any()
//            )
//        ).thenThrow(
//            StatusRuntimeException(Status.FAILED_PRECONDITION)
//        )
//
//
//        // exec
//        val exception = Assertions.assertThrows(HttpClientResponseException::class.java){
//            client.toBlocking().exchange(request, RegistraPixRequest::class.java)
//        }
//
//        // validacao
//        Assertions.assertNotNull(exception)
//        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.status)
//
//    }
//
//    @Test
//    fun `Deve retornar Codigo 409 Conflict quando o stub grpc retornar um codigo ALREADY EXISTS`(){
//
//        // cenario
//        val request = HttpRequest.POST(
//            UriBuilder.of("/api/v1/clientes/$ID_TITULAR_VALIDO/pix").build(),
//            chavePixValida()
//        )
//
//        Mockito.`when`(
//            registraPixStub.registra(
//                Mockito.any()
//            )
//        ).thenThrow(
//            StatusRuntimeException(Status.ALREADY_EXISTS)
//        )
//
//
//        // exec
//        val exception = Assertions.assertThrows(HttpClientResponseException::class.java){
//            client.toBlocking().exchange(request, RegistraPixRequest::class.java)
//        }
//
//        // validacao
//        Assertions.assertNotNull(exception)
//        Assertions.assertEquals(HttpStatus.CONFLICT, exception.status)
//    }


    private fun chavePixValida() : RegistraPixRequest {
        return RegistraPixRequest(
            tipoChave = TipoChave.CPF,
            tipoConta = TipoConta.CONTA_CORRENTE,
            chave = CPF_VALIDO
        )
    }

    private fun chavePixTotalmenteInvalida() : RegistraPixRequest {
        return RegistraPixRequest(
            tipoChave = null,
            tipoConta = null,
            chave = null
        )
    }

    @Factory
    internal class PixGrpcMockStubFactory{

        @Singleton
        @Replaces(value = KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub::class)
        fun mockStub(): KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub = Mockito.mock(KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub::class.java)

    }

}



