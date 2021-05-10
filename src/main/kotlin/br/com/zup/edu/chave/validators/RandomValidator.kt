package br.com.zup.edu.chave.validators

import br.com.zup.edu.TipoChave
import br.com.zup.edu.chave.CriaChaveRequest
import javax.inject.Singleton

@Singleton
class RandomValidator: PixValidator {
    override fun supports(tipo: TipoChave): Boolean {
        return tipo == TipoChave.RANDOM
    }

    override fun validate(request: CriaChaveRequest) = request.chave.isBlank()
}