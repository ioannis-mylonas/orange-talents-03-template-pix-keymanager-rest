package br.com.zup.edu.chave

import br.com.zup.edu.GetKeyResponse

data class Instituicao(
    val nome: String,
    val agencia: String,
    val conta: String,
    val tipoConta: TipoContaRequest
) {
    constructor(instituicao: GetKeyResponse.Instituicao): this(instituicao.nome,
        instituicao.agencia, instituicao.conta,
        TipoContaRequest.fromResponse(instituicao.tipoConta))
}
