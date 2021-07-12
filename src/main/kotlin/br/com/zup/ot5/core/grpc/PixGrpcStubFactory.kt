package br.com.zup.ot5.core.grpc

import br.com.zup.ot5.KeyManagerConsultaServiceGrpc
import br.com.zup.ot5.KeyManagerExcluiServiceGrpc
import br.com.zup.ot5.KeyManagerListaServiceGrpc
import br.com.zup.ot5.KeyManagerRegistraServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class PixGrpcStubFactory {

    @Singleton
    fun registraPixStub(@GrpcChannel("pix") channel: ManagedChannel)
            : KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub{
        return KeyManagerRegistraServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun excluiPixStub(@GrpcChannel("pix") channel: ManagedChannel)
            : KeyManagerExcluiServiceGrpc.KeyManagerExcluiServiceBlockingStub{
        return KeyManagerExcluiServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun consultaPixStub(@GrpcChannel("pix") channel: ManagedChannel)
            : KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub{
        return KeyManagerConsultaServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun listaPixStub(@GrpcChannel("pix") channel: ManagedChannel)
            : KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub{
        return KeyManagerListaServiceGrpc.newBlockingStub(channel)
    }

}