package br.com.zup.edu.chave.validators

import br.com.zup.edu.TipoChave
import br.com.zup.edu.chave.CriaChaveRequest
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.Validator
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Singleton
class EmailValidator(@Inject val validator: Validator): PixValidator {
    override fun supports(tipo: TipoChave): Boolean {
        return tipo == TipoChave.EMAIL
    }

    override fun validate(request: CriaChaveRequest): Boolean {
        val email = EmailRequest(request.chave)
        return validator.validate(email).isEmpty()
    }

    @Introspected
    class EmailRequest(
        @field:Email @field:NotBlank @field:Size(max = 77) val email: String
    )
}