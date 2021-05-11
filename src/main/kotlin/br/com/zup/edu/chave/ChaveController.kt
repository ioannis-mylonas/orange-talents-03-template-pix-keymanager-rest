package br.com.zup.edu.chave

import br.com.zup.edu.DeleteKeyRequest
import br.com.zup.edu.GetKeyRequest
import br.com.zup.edu.KeymanagerGRPCServiceGrpc
import br.com.zup.edu.ListKeyRequest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Controller("/api/clientes/{id}/pix")
class ChaveController(
        @Inject val rpc: KeymanagerGRPCServiceGrpc.KeymanagerGRPCServiceBlockingStub,
        @Inject val statusMapper: StatusRuntimeExceptionMapper
    ) {

    @Post
    fun cadastra(@Body @Valid request: CriaChaveRequest, @PathVariable id: UUID): HttpResponse<Any> {
        return try {
            val response = rpc.cria(request.converte(id))
            val location = HttpResponse.uri("/api/clientes/$id/pix/${response.idPix}")
            HttpResponse.created(location)
        } catch (e: StatusRuntimeException) {
            return statusMapper.map(e)
        }
    }

    @Delete("/{idPix}")
    fun exclui(@PathVariable id: UUID, @PathVariable idPix: Long): HttpResponse<*> {
        return try {
            rpc.delete(
                DeleteKeyRequest.newBuilder()
                .setIdCliente(id.toString())
                .setIdPix(idPix)
                .build()
            )
            
            return HttpResponse.ok<Any>()
        } catch (e: StatusRuntimeException) {
            statusMapper.map(e)
        }
    }

    @Get("/{idPix}")
    fun detalhes(@PathVariable id: UUID, @PathVariable idPix: Long): HttpResponse<*> {
        return try {
            val response = rpc.get(
                GetKeyRequest.newBuilder()
                .setIdCliente(id.toString())
                .setIdPix(idPix)
                .build()
            )

            HttpResponse.ok(Chave(response))
        } catch (e: StatusRuntimeException) {
            statusMapper.map(e)
        }
    }

    @Get
    fun list(@PathVariable id: UUID): HttpResponse<*> {
        return try {
            val response = rpc.list(
                ListKeyRequest.newBuilder()
                    .setIdCliente(id.toString())
                    .build()
            )

            HttpResponse.ok(response.chavesList.map { Chave(it) })
        } catch (e: StatusRuntimeException) {
            statusMapper.map(e)
        }
    }
}