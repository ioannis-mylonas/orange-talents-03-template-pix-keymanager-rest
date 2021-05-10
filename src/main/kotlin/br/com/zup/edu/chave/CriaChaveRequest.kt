package br.com.zup.edu.chave

import br.com.zup.edu.CreateKeyRequest
import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
class CriaChaveRequest(
    @JsonProperty @field:NotNull val tipoChave: TipoChave,
    @JsonProperty @field:NotNull val tipoConta: TipoConta,
    @JsonProperty @field:NotBlank val idCliente: String,
    chave: String?
) {
    @JsonProperty @field:Size(max = 77) val chave: String = chave ?: ""

    fun converte(): CreateKeyRequest {
        return CreateKeyRequest.newBuilder()
            .setTipoChave(tipoChave)
            .setChave(chave)
            .setTipoConta(tipoConta)
            .setIdCliente(idCliente)
            .build()
    }
}