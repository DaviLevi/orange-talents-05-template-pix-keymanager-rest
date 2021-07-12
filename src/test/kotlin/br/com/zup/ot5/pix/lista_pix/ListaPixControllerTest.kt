package br.com.zup.ot5.pix.lista_pix

import br.com.zup.ot5.*
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class ListaPixControllerTest{

    @field:Inject
    private lateinit var listaPixStub: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub

    @field:Inject
    @field:Client("/")
    private lateinit var client: HttpClient


    companion object{
        const val ID_TITULAR_VALIDO = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val PIX_ID_VALIDO_1 = UUID.randomUUID().toString()
        val PIX_ID_VALIDO_2 = UUID.randomUUID().toString()
        val PIX_ID_VALIDO_3 = UUID.randomUUID().toString()
        val CPF_VALIDO = "02467781054"
        val EMAIL_VALIDO = "davide.sgalambro@zup.com.br"

    }

    @Test
    fun `Deve listar as chaves pix de um titular quando existentes`(){

        // cenario
        val request = HttpRequest.GET<Any>("/api/v1/clientes/${ID_TITULAR_VALIDO}/pix")

        Mockito.`when`(
            listaPixStub.listaChavePix(
                ListaChavePixRequest.newBuilder().setIdTitular(ID_TITULAR_VALIDO).build()
            )
        ).thenReturn(
            ListaChavePixResponse.newBuilder().addAllChaves(
                listOf(
                    ResumoChavePix.newBuilder()
                        .setPixId(PIX_ID_VALIDO_1)
                        .setIdTitular(ID_TITULAR_VALIDO)
                        .setTipoChave(TipoChave.CPF)
                        .setValorChave(CPF_VALIDO)
                        .setTipoConta(TipoConta.CONTA_CORRENTE)
                        .setCriadaEm(LocalDateTime.now().minusDays(1L).toString())
                    .build(),
                    ResumoChavePix.newBuilder()
                        .setPixId(PIX_ID_VALIDO_2)
                        .setIdTitular(ID_TITULAR_VALIDO)
                        .setTipoChave(TipoChave.EMAIL)
                        .setValorChave(EMAIL_VALIDO)
                        .setTipoConta(TipoConta.CONTA_CORRENTE)
                        .setCriadaEm(LocalDateTime.now().minusDays(2L).toString())
                        .build()
                )
            ).build()
        )

        // exec
        val response = client.toBlocking().exchange(request, List::class.java)

        // validacao
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response.status)
        Assertions.assertEquals(2, response?.body()?.size)
    }

    @Test
    fun `Deve apresentar uma lista de chaves vazia quando o titular nao possuir nenhuma chave`(){

        // cenario
        val request = HttpRequest.GET<Any>("/api/v1/clientes/${ID_TITULAR_VALIDO}/pix")

        Mockito.`when`(
            listaPixStub.listaChavePix(
                ListaChavePixRequest.newBuilder().setIdTitular(ID_TITULAR_VALIDO).build()
            )
        ).thenReturn(
            ListaChavePixResponse.newBuilder().addAllChaves(
                listOf()
            ).build()
        )

        // exec
        val response = client.toBlocking().exchange(request, List::class.java)

        // validacao
        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK, response.status)
        Assertions.assertTrue(response?.body()?.isEmpty()!!)
    }

    @Factory
    internal class PixGrpcMockStubFactory{

        @Singleton
        @Replaces(value = KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub::class)
        fun mockStub(): KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub = Mockito.mock(
            KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub::class.java)

    }

}