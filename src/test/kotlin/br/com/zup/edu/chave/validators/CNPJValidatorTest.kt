package br.com.zup.edu.chave.validators

import br.com.zup.edu.TipoChave
import br.com.zup.edu.chave.CriaChaveRequest
import br.com.zup.edu.chave.TipoChaveRequest
import br.com.zup.edu.chave.TipoContaRequest
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import javax.inject.Inject

@MicronautTest
internal class CNPJValidatorTest {
    @Inject
    lateinit var validator: CNPJValidator

    private val tipoChave = TipoChaveRequest.CPF
    private val tipoConta = TipoContaRequest.CONTA_CORRENTE

    @ParameterizedTest
    @ValueSource(strings = ["88171435000140", "48364486000173", "75966136000150", "75966136000150"])
    fun `testa chave valida`(chave: String) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertTrue(validator.validate(request))
    }

    @ParameterizedTest
    @ValueSource(strings = ["8817143500014", "1", "4099003500013", "41.807.181/0001-40", "abcdefghijk"])
    @NullAndEmptySource
    fun `testa chaves invalidas`(chave: String?) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertFalse(validator.validate(request))
    }

    @Test
    fun `testa suporta tipo correto`() {
        assertTrue(validator.supports(TipoChave.CNPJ))
    }
}