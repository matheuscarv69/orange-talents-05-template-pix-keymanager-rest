package br.com.zup.matheuscarv69.grpcClients

import br.com.zup.matheuscarv69.KeyManagerRegistraGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun registraClientStud(@GrpcChannel("registra") channel: ManagedChannel)
            : KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub? {

        return KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)
    }


}