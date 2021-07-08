package br.com.zup.ot5.core.grpc

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

}