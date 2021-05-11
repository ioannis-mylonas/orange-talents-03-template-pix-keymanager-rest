package br.com.zup.edu.chave.validators

import br.com.zup.edu.TipoChave
import br.com.zup.edu.chave.CriaChaveRequest
import br.com.zup.edu.chave.TipoChaveRequest
import br.com.zup.edu.chave.TipoContaRequest
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import javax.inject.Inject

@MicronautTest
internal class RandomValidatorTest {
    @Inject
    lateinit var validator: RandomValidator

    private val tipoChave = TipoChaveRequest.RANDOM
    private val tipoConta = TipoContaRequest.CONTA_CORRENTE

    @ParameterizedTest
    @NullAndEmptySource
    fun `testa chave valida`(chave: String?) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertTrue(validator.validate(request))
    }

    @ParameterizedTest
    @ValueSource(strings = ["+1086780500648429", "ufefuffa-3283@yopmail.com",
        "14312464090", "1", "88171435000140"])
    fun `testa chaves invalidas`(chave: String) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertFalse(validator.validate(request))
    }

    @Test
    fun `testa suporta tipo correto`() {
        assertTrue(validator.supports(TipoChave.RANDOM))
    }
}