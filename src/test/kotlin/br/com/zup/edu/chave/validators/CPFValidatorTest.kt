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
internal class CPFValidatorTest {
    @Inject
    lateinit var validator: CPFValidator

    private val tipoChave = TipoChaveRequest.CPF
    private val tipoConta = TipoContaRequest.CONTA_CORRENTE

    @ParameterizedTest
    @ValueSource(strings = ["14312464090", "54158613000", "18423574091", "17459891029"])
    fun `testa chave valida`(chave: String) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertTrue(validator.validate(request))
    }

    @ParameterizedTest
    @ValueSource(strings = ["1234567890", "1", "5134213456", "3751607102", "abcdefghijk"])
    @NullAndEmptySource
    fun `testa chaves invalidas`(chave: String?) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertFalse(validator.validate(request))
    }

    @Test
    fun `testa suporta tipo correto`() {
        assertTrue(validator.supports(TipoChave.CPF))
    }
}