package br.com.zup.edu.chave

import br.com.zup.edu.CreateKeyRequest
import br.com.zup.edu.chave.validators.ChavePix
import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ChavePix
class CriaChaveRequest(
    @JsonProperty @field:NotNull val tipoChave: TipoChaveRequest,
    @JsonProperty @field:NotNull val tipoConta: TipoContaRequest,
    chave: String?
) {
    @JsonProperty @field:Size(max = 77) val chave: String = chave ?: ""

    fun converte(idCliente: UUID): CreateKeyRequest {
        return CreateKeyRequest.newBuilder()
            .setTipoChave(tipoChave.valor)
            .setChave(chave)
            .setTipoConta(tipoConta.valor)
            .setIdCliente(idCliente.toString())
            .build()
    }
}