package br.com.zup.edu.chave

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import javax.validation.Valid

@Controller("/api/chaves")
class ChaveController {
    @Post
    fun cadastra(@Body @Valid request: CriaChaveRequest) {
        println(request)
    }
}