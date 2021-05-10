package br.com.zup.edu.chave

import br.com.zup.edu.TipoConta

enum class TipoContaRequest(val valor: TipoConta) {
    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)
}