package br.com.zup.ot5.pix.exclui_pix

import br.com.zup.ot5.*
import br.com.zup.ot5.pix.registra_pix.RegistraPixRequest
import br.com.zup.ot5.pix.registra_pix.TipoChave
import br.com.zup.ot5.pix.registra_pix.TipoConta
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class ExcluiPixControllerTest{

    @field:Inject
    private lateinit var excluiPixStub: KeyManagerExcluiServiceGrpc.KeyManagerExcluiServiceBlockingStub

    @field:Inject
    @field:Client("/")
    private lateinit var client: HttpClient


    companion object{
        const val ID_TITULAR_VALIDO = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        const val PIX_ID_VALIDO = "261b9877-f31a-4487-bade-a136232399a9"
    }

    @Test
    fun `Deve remover uma chave pix`(){

        // cenario
        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$ID_TITULAR_VALIDO/pix/$PIX_ID_VALIDO")

        Mockito.`when`(
            excluiPixStub.excluiChavePix(
                ExcluiChavePixRequest
                    .newBuilder()
                        .setPixId(PIX_ID_VALIDO)
                        .setIdTitular(ID_TITULAR_VALIDO)
                    .build()
            )
        ).thenReturn(
            ExcluiChavePixResponse.newBuilder().build()
        )

        // exec
        val response = client.toBlocking().exchange(request, Any::class.java)

        // validacao
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response.status)


    }

    @Factory
    internal class PixGrpcMockStubFactory{

        @Singleton
        @Replaces(value = KeyManagerExcluiServiceGrpc.KeyManagerExcluiServiceBlockingStub::class)
        fun mockStub(): KeyManagerExcluiServiceGrpc.KeyManagerExcluiServiceBlockingStub = Mockito.mock(
            KeyManagerExcluiServiceGrpc.KeyManagerExcluiServiceBlockingStub::class.java)

    }

}