package br.com.zup.matheuscarv69.grpcClients

import br.com.zup.matheuscarv69.KeyManagerDetalhaChaveServiceGrpc
import br.com.zup.matheuscarv69.KeyManagerListaChavesServiceGrpc
import br.com.zup.matheuscarv69.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.matheuscarv69.KeyManagerRemoverServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun registraClientStub(@GrpcChannel("keymanager") channel: ManagedChannel) =
        KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removerClientStub(@GrpcChannel("keymanager") channel: ManagedChannel) =
        KeyManagerRemoverServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun detalharClientStub(@GrpcChannel("keymanager") channel: ManagedChannel) =
        KeyManagerDetalhaChaveServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaClientStub(@GrpcChannel("keymanager") channel: ManagedChannel) =
        KeyManagerListaChavesServiceGrpc.newBlockingStub(channel)


}