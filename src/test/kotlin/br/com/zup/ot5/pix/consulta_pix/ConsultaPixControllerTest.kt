package br.com.zup.ot5.pix.consulta_pix

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
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class ConsultaPixControllerTest{

    @field:Inject
    private lateinit var consultaPixStub: KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub

    @field:Inject
    @field:Client("/")
    private lateinit var client: HttpClient


    companion object{
        const val ID_TITULAR_VALIDO = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        const val PIX_ID_VALIDO = "261b9877-f31a-4487-bade-a136232399a9"
    }

    @Test
    fun `Deve consultar uma chave pix`(){

        // cenario
        val request = HttpRequest.GET<Any>("/api/v1/clientes/${ID_TITULAR_VALIDO}/pix/${PIX_ID_VALIDO}")

        Mockito.`when`(
            consultaPixStub.consultaChavePix(
                ConsultaChavePixRequest
                    .newBuilder()
                    .setPixId(PIX_ID_VALIDO)
                    .setIdTitular(ID_TITULAR_VALIDO)
                    .build()
            )
        ).thenReturn(
            ConsultaChavePixResponse
            .newBuilder()
                .setPixId(PIX_ID_VALIDO)
                .setIdTitular(ID_TITULAR_VALIDO)
                .setTipoChave(TipoChave.CPF)
                .setValorChave("02467781054")
                .setNomeTitular("Rafael M C Ponte")
                .setCpfTitular("02467781054")
                .setInstituicaoConta("ITAÚ UNIBANCO S.A.")
                .setIspb("60701190")
                .setAgencia("0001")
                .setNumero("291900")
                .setTipoConta(TipoConta.CONTA_CORRENTE)
                .setCriadaEm(LocalDateTime.now().toString())
            .build()
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
        @Replaces(value = KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub::class)
        fun mockStub(): KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub = Mockito.mock(
            KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub::class.java)

    }

}