package br.com.zup.edu.chave

import br.com.zup.edu.GetKeyResponse

data class Titular(
    val nome: String,
    val cpf: String
) {
    constructor(titular: GetKeyResponse.Titular): this(titular.nome, titular.cpf)
}