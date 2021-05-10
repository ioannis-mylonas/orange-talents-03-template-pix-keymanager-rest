package br.com.zup.edu.chave.validators

import br.com.zup.edu.TipoChave
import br.com.zup.edu.chave.CriaChaveRequest
import javax.inject.Singleton
import javax.validation.ConstraintViolationException
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Singleton
class EmailValidator: PixValidator {
    override fun supports(tipo: TipoChave): Boolean {
        return tipo == TipoChave.EMAIL
    }

    override fun validate(request: CriaChaveRequest): Boolean {
        return try {
            check(request.chave)
            true
        } catch (e: ConstraintViolationException) {
            false
        }
    }

    private fun check(@Email @NotBlank @Size(max = 77) email: String) {}
}