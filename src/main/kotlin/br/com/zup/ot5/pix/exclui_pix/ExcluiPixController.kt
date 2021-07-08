package br.com.zup.ot5.pix.exclui_pix

import br.com.zup.ot5.ExcluiChavePixRequest
import br.com.zup.ot5.KeyManagerExcluiServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.*

@Controller("/api/v1/clientes/{idTitular}/pix/{pixId}")
@Validated
class ExcluiPixController(
    private val excluiPixGrpcStub: KeyManagerExcluiServiceGrpc.KeyManagerExcluiServiceBlockingStub
){

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Delete
    fun registraPix(@PathVariable idTitular: UUID,
                    @PathVariable pixId: UUID
    ) : HttpResponse<Any> {

        excluiPixGrpcStub.excluiChavePix(
            ExcluiChavePixRequest.newBuilder()
                .setIdTitular(idTitular.toString())
                .setPixId(pixId.toString())
            .build()
        )

        logger.info("Chave pix com id '$pixId' excluida com sucesso")

        return HttpResponse.ok()
    }

}