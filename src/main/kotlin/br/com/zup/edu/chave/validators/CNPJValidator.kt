package br.com.zup.edu.chave.validators

import br.com.zup.edu.TipoChave
import br.com.zup.edu.chave.CriaChaveRequest
import javax.inject.Singleton

@Singleton
class CNPJValidator: PixValidator {
    override fun supports(tipo: TipoChave): Boolean {
        return tipo == TipoChave.CNPJ
    }

    override fun validate(request: CriaChaveRequest) = request.chave.matches("^[0-9]{14}\$".toRegex())
}