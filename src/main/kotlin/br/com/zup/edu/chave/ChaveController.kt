package br.com.zup.edu.chave

import br.com.zup.edu.KeymanagerGRPCServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import javax.inject.Inject
import javax.validation.Valid

@Controller("/api/chaves")
class ChaveController(
        @Inject val rpc: KeymanagerGRPCServiceGrpc.KeymanagerGRPCServiceBlockingStub
    ) {
    @Post
    fun cadastra(@Body @Valid request: CriaChaveRequest): HttpResponse<*> {
        return try {
            val response = rpc.cria(request.converte())
            HttpResponse.ok<Any>("PIX ID: ${response.idPix}")
        } catch (e: StatusRuntimeException) {
            when (e.status.code) {
                Status.NOT_FOUND.code -> HttpResponse.notFound(e.message)
                Status.PERMISSION_DENIED.code -> HttpResponse.unauthorized<Any>().body(e.message)
                Status.INVALID_ARGUMENT.code -> HttpResponse.badRequest(e.message)
                Status.ALREADY_EXISTS.code -> HttpResponse.unprocessableEntity<Any>().body(e.message)
                else -> throw e
            }
        }
    }
}