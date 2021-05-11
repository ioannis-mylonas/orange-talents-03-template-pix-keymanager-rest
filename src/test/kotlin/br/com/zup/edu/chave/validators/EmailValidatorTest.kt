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
internal class EmailValidatorTest {
    @Inject
    lateinit var validator: EmailValidator

    private val tipoChave = TipoChaveRequest.EMAIL
    private val tipoConta = TipoContaRequest.CONTA_CORRENTE

    @ParameterizedTest
    @ValueSource(strings = ["ufefuffa-3283@yopmail.com", "staffelb@gmail.com",
        "jmmuller@verizon.net", "mobileip@optonline.net"])
    fun `testa chave valida`(chave: String) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertTrue(validator.validate(request))
    }

    @ParameterizedTest
    @ValueSource(strings = ["mobileip", "0", "phizntrg@", "yahoo.ca", "icloud.com"])
    @NullAndEmptySource
    fun `testa chaves invalidas`(chave: String?) {
        val request = CriaChaveRequest(tipoChave, tipoConta, chave)
        assertFalse(validator.validate(request))
    }

    @Test
    fun `testa suporta tipo correto`() {
        assertTrue(validator.supports(TipoChave.EMAIL))
    }
}