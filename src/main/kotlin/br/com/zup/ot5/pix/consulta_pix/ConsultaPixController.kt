package br.com.zup.ot5.pix.consulta_pix

import br.com.zup.ot5.ConsultaChavePixRequest
import br.com.zup.ot5.KeyManagerConsultaServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.constraints.NotNull

@Controller("/api/v1/clientes/{idTitular}/pix/{pixId}")
@Validated
class ConsultaPixController(
    private val consultaPixGrpcStub: KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub
){

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get
    fun registraPix(@PathVariable @NotNull idTitular: UUID,
                    @PathVariable @NotNull pixId: UUID
    ) : HttpResponse<Any> {

        val response = consultaPixGrpcStub.consultaChavePix(
            ConsultaChavePixRequest.newBuilder()
                .setIdTitular(idTitular.toString())
                .setPixId(pixId.toString())
                .build()
        )

        logger.info("Chave pix com id '$pixId' consultada com sucesso")

        return HttpResponse.ok(
            ConsultaPixResponse(response)
        )
    }

}