package br.com.zup.edu.chave

import br.com.zup.edu.TipoChave

enum class TipoChaveRequest(val valor: TipoChave) {
    RANDOM(TipoChave.RANDOM),
    CPF(TipoChave.CPF),
    EMAIL(TipoChave.EMAIL),
    PHONE(TipoChave.PHONE)
}