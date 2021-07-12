package br.com.zup.ot5.pix.lista_pix

import br.com.zup.ot5.KeyManagerListaServiceGrpc
import br.com.zup.ot5.ListaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.constraints.NotNull

@Controller("/api/v1/clientes/{idTitular}/pix")
@Validated
class ListaPixController(
    private val listaPixGrpcStub: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub
){

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get
    fun registraPix(@PathVariable @NotNull idTitular: UUID
    ) : HttpResponse<Any> {

        val response = listaPixGrpcStub.listaChavePix(
            ListaChavePixRequest.newBuilder()
                .setIdTitular(idTitular.toString())
                .build()
        )

        logger.info("Consultadas chaves pix do titular com id '${idTitular.toString()}'")

        return HttpResponse.ok(
            response.chavesList.map { ResumoPixResponse(it) }
        )
    }

}