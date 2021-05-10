package br.com.zup.edu.chave.validators

import br.com.zup.edu.TipoChave
import br.com.zup.edu.chave.CriaChaveRequest
import javax.inject.Singleton

@Singleton
class PhoneValidator: PixValidator {
    override fun supports(tipo: TipoChave): Boolean {
        return tipo == TipoChave.PHONE
    }

    override fun validate(request: CriaChaveRequest) = request.chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
}