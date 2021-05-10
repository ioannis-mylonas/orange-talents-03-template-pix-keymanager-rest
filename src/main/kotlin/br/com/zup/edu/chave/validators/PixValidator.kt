package br.com.zup.edu.chave.validators

import br.com.zup.edu.TipoChave
import br.com.zup.edu.chave.CriaChaveRequest

interface PixValidator {
    fun supports(tipo: TipoChave): Boolean
    fun validate(request: CriaChaveRequest): Boolean
}