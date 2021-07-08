package br.com.zup.ot5.pix.registra_pix

import br.com.zup.ot5.KeyManagerRegistraServiceGrpc
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.*
import javax.validation.Valid

@Controller("/api/v1/clientes/{idTitular}/pix")
@Validated
class RegistraPixController(
    private val registraPixGrpcStub: KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub
){

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post
    fun registraPix(@PathVariable idTitular: UUID,
                    @Valid @Body requisicao: RegistraPixRequest) : HttpResponse<Any>{

        val grpcResponse = registraPixGrpcStub.registra(requisicao.paraGrpcRequest(idTitular.toString()))

        logger.info("Chave '${requisicao.chave}' registrada com sucesso")

        return HttpResponse.created(
            URI.create("/api/v1/clientes/$idTitular/pix/${grpcResponse.pixId}")
        )
    }

}