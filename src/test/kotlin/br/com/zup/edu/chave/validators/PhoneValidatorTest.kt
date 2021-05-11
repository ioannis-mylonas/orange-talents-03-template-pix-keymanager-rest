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
internal class PhoneValidatorTest {
    @Inject
    lateinit var validator: PhoneValidator

    private val tipoChave = TipoChaveRequest.PHONE
    private val tipoConta = TipoContaRequest.CONTA_CORRENTE

    @ParameterizedTest
    @ValueSource(strings = ["+100", "+990", "+1086780500648429", "+9962143604331843"])
    fun `testa chave valida`(chave: String) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertTrue(validator.validate(request))
    }

    @ParameterizedTest
    @ValueSource(strings = ["71032685918120", "+16", "+11710326859181201", "abcde", "telefone", "+"])
    @NullAndEmptySource
    fun `testa chaves invalidas`(chave: String?) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertFalse(validator.validate(request))
    }

    @Test
    fun `testa suporta tipo correto`() {
        assertTrue(validator.supports(TipoChave.PHONE))
    }
}