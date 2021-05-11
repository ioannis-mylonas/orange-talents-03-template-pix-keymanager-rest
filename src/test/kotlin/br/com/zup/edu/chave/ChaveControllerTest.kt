package br.com.zup.edu.chave

import br.com.zup.edu.CreateKeyResponse
import br.com.zup.edu.KeymanagerGRPCServiceGrpc
import io.grpc.Status
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ChaveControllerTest {
    @Inject
    @field:Client("/")
    lateinit var http: HttpClient

    @Inject
    lateinit var rpc: KeymanagerGRPCServiceGrpc.KeymanagerGRPCServiceBlockingStub

    @AfterEach
    fun teardown() {
        Mockito.reset(rpc)
    }

    @ParameterizedTest
    @ValueSource(strings = ["14312464090", "54158613000", "18423574091", "17459891029"])
    fun `testa cadastro chave valida`(key: String) {
        val idCliente = UUID.randomUUID().toString()
        val request = CriaChaveRequest(TipoChaveRequest.CPF, TipoContaRequest.CONTA_CORRENTE, key)

        val response = CreateKeyResponse.newBuilder().setIdPix(1L).build()
        BDDMockito.given(rpc.cria(Mockito.any())).willReturn(response)
        val result = http.toBlocking().exchange<CriaChaveRequest, Any>(HttpRequest.POST("/api/clientes/${idCliente}/pix", request))

        assertEquals(HttpStatus.CREATED, result.status)
        assertTrue(result.headers.contains("Location"))
        assertTrue(result.header("Location")!!.contains(response.idPix.toString()))
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = ["1234567890", "1", "5134213456", "3751607102"])
    fun `testa cadastro chave invalida`(key: String?) {
        val idCliente = UUID.randomUUID().toString()
        val request = CriaChaveRequest(TipoChaveRequest.CPF, TipoContaRequest.CONTA_CORRENTE, key)

        val response = CreateKeyResponse.newBuilder().setIdPix(1L).build()
        BDDMockito.given(rpc.cria(Mockito.any())).willReturn(response)

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking()
                .exchange<CriaChaveRequest, Any>(HttpRequest.POST("/api/clientes/${idCliente}/pix", request))
        }

        assertEquals(HttpStatus.BAD_REQUEST.code, erro.status.code)
    }

    @Test
    fun `testa erro rpc criacao`() {
        BDDMockito.given(rpc.cria(Mockito.any())).willThrow(Status.ALREADY_EXISTS.asRuntimeException())

        val idCliente = UUID.randomUUID().toString()
        val request = CriaChaveRequest(TipoChaveRequest.CPF, TipoContaRequest.CONTA_CORRENTE, "18423574091")

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking()
                .exchange<CriaChaveRequest, Any>(HttpRequest.POST("/api/clientes/${idCliente}/pix", request))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.code, erro.status.code)
    }

    @Test
    fun `testa criacao nao encontra usuario invalido`() {
        BDDMockito.given(rpc.cria(Mockito.any())).willThrow(Status.ALREADY_EXISTS.asRuntimeException())

        val idCliente = "abcde"
        val request = CriaChaveRequest(TipoChaveRequest.CPF, TipoContaRequest.CONTA_CORRENTE, "18423574091")

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking()
                .exchange<CriaChaveRequest, Any>(HttpRequest.POST("/api/clientes/${idCliente}/pix", request))
        }

        assertEquals(HttpStatus.BAD_REQUEST.code, erro.status.code)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockFactory {
        @Singleton
        fun rpc() = Mockito.mock(KeymanagerGRPCServiceGrpc.KeymanagerGRPCServiceBlockingStub::class.java)
    }
}