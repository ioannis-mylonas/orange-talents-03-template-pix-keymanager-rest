package br.com.zup.edu.chave

import br.com.zup.edu.GetKeyResponse

data class Chave(
    val idPix: Long,
    val idCliente: String,
    val tipoChave: TipoChaveRequest,
    val chave: String,
    val titular: Titular,
    val instituicaoval: Instituicao,
    val criadaEm: String
) {
    constructor(chave: GetKeyResponse): this(chave.idPix, chave.idCliente,
        TipoChaveRequest.fromResponse(chave.tipoChave), chave.chave,
        Titular(chave.titular), Instituicao(chave.instituicao), chave.criadaEm)
}