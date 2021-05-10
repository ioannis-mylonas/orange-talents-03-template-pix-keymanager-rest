package br.com.zup.edu.chave

import br.com.zup.edu.TipoChave

enum class TipoChaveRequest(val valor: TipoChave) {
    RANDOM(TipoChave.RANDOM),
    CPF(TipoChave.CPF),
    EMAIL(TipoChave.EMAIL),
    PHONE(TipoChave.PHONE);

    companion object {
        fun fromResponse(tipo: TipoChave): TipoChaveRequest {
            return when(tipo) {
                TipoChave.RANDOM -> RANDOM
                TipoChave.CPF -> CPF
                TipoChave.EMAIL -> EMAIL
                TipoChave.PHONE -> PHONE
                else -> throw EnumConstantNotPresentException(TipoChaveRequest::class.java, tipo.name)
            }
        }
    }
}