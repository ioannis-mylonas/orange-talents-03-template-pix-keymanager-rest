package br.com.zup.edu.chave

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import javax.inject.Singleton

@Singleton
class StatusRuntimeExceptionMapper {
    fun map(e: StatusRuntimeException): HttpResponse<Any> {
        return when (e.status.code) {
            Status.NOT_FOUND.code -> HttpResponse.notFound(e.message)
            Status.PERMISSION_DENIED.code -> HttpResponse.unauthorized<Any>().body(e.message)
            Status.INVALID_ARGUMENT.code -> HttpResponse.badRequest(e.message)
            Status.ALREADY_EXISTS.code -> HttpResponse.unprocessableEntity<Any>().body(e.message)
            else -> throw e
        }
    }
}