package br.com.zup.edu.chave

import br.com.zup.edu.KeymanagerGRPCServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {
    @Singleton
    fun keymanagerGRPCServiceBlockingStub(
        @GrpcChannel("\${keymanager.server-url}") channel: ManagedChannel
    ): KeymanagerGRPCServiceGrpc.KeymanagerGRPCServiceBlockingStub {
        return KeymanagerGRPCServiceGrpc.newBlockingStub(channel)
    }
}