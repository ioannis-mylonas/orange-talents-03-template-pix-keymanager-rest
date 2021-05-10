package br.com.zup.edu.chave

import br.com.zup.edu.TipoConta

enum class TipoContaRequest(val valor: TipoConta) {
    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA);

    companion object {
        fun fromResponse(tipo: TipoConta): TipoContaRequest {
            return when(tipo) {
                TipoConta.CONTA_CORRENTE -> CONTA_CORRENTE
                TipoConta.CONTA_POUPANCA -> CONTA_POUPANCA
                else -> throw EnumConstantNotPresentException(TipoContaRequest::class.java, tipo.name)
            }
        }
    }
}