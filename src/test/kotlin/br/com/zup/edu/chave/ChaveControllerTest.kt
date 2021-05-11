package br.com.zup.edu.chave

import br.com.zup.edu.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@MicronautTest
internal class ChaveControllerTest {
    @Inject
    @field:Client("/")
    lateinit var http: HttpClient

    @Inject
    lateinit var rpc: KeymanagerGRPCServiceGrpc.KeymanagerGRPCServiceBlockingStub

    @Inject
    lateinit var mapper: ObjectMapper

    @AfterEach
    fun teardown() {
        Mockito.reset(rpc)
    }

    @Test
    fun `testa cadastro chave valida`() {
        val idCliente = UUID.randomUUID().toString()
        val request = CriaChaveRequest(TipoChaveRequest.CPF, TipoContaRequest.CONTA_CORRENTE, "14312464090")

        val response = CreateKeyResponse.newBuilder().setIdPix(1L).build()
        BDDMockito.given(rpc.cria(Mockito.any())).willReturn(response)
        val result = http.toBlocking()
            .exchange<CriaChaveRequest, Any>(HttpRequest.POST("/api/clientes/${idCliente}/pix", request))

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
                .exchange<CriaChaveRequest, Any>(HttpRequest.POST("/api/clientes/$idCliente/pix", request))
        }

        assertEquals(HttpStatus.BAD_REQUEST.code, erro.status.code)
    }

    @Test
    fun `testa exclusao de chave`() {
        val idCliente = UUID.randomUUID().toString()
        val idPix = Random.nextLong()
        val response = http.toBlocking()
            .exchange<Unit, Unit>(HttpRequest.DELETE("/api/clientes/$idCliente/pix/$idPix"))

        assertEquals(HttpStatus.OK.code, response.status.code)
    }

    @Test
    fun `testa falha exclusao chave inexistente`() {
        val idCliente = UUID.randomUUID().toString()
        val idPix = Random.nextLong()

        BDDMockito.given(rpc.delete(Mockito.any())).willThrow(Status.NOT_FOUND.asRuntimeException())

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking()
                .exchange<Unit, Unit>(HttpRequest.DELETE("/api/clientes/$idCliente/pix/$idPix"))
        }

        assertEquals(HttpStatus.NOT_FOUND.code, erro.status.code)
    }

    @Test
    fun `testa falha exclusao chave sem permissao`() {
        val idCliente = UUID.randomUUID().toString()
        val idPix = Random.nextLong()

        BDDMockito.given(rpc.delete(Mockito.any())).willThrow(Status.PERMISSION_DENIED.asRuntimeException())

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking()
                .exchange<Unit, Unit>(HttpRequest.DELETE("/api/clientes/$idCliente/pix/$idPix"))
        }

        assertEquals(HttpStatus.FORBIDDEN.code, erro.status.code)
    }

    @Test
    fun `testa falha exclusao id invalido`() {
        val idCliente = UUID.randomUUID().toString()
        val idPix = "abcd"

        BDDMockito.given(rpc.delete(Mockito.any())).willThrow(Status.PERMISSION_DENIED.asRuntimeException())

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking()
                .exchange<Unit, Unit>(HttpRequest.DELETE("/api/clientes/$idCliente/pix/$idPix"))
        }

        assertEquals(HttpStatus.BAD_REQUEST.code, erro.status.code)
    }

    @Test
    fun `testa busca detalhes chave especifica`() {
        val idCliente = UUID.randomUUID().toString()
        val idPix = Random.nextLong()

        val response = GetKeyResponse.newBuilder()
            .setChave(UUID.randomUUID().toString())
            .setCriadaEm(LocalDateTime.now().toString())
            .setIdCliente(idCliente)
            .setIdPix(idPix)
            .setTipoChave(TipoChave.RANDOM)
            .setTitular(
                GetKeyResponse.Titular.newBuilder()
                    .setNome("Nome")
                    .setCpf("CPF")
                    .build()
            ).setInstituicao(
                GetKeyResponse.Instituicao.newBuilder()
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .setNome("Instituição")
                    .setConta("Conta")
                    .setAgencia("Agencia")
                    .build()
            ).build()

        BDDMockito.given(rpc.get(Mockito.any())).willReturn(response)

        val result = http.toBlocking().retrieve("/api/clientes/$idCliente/pix/$idPix")
        val key = mapper.readValue(result, Chave::class.java)

        assertEquals(idPix, key.idPix)
        assertEquals(idCliente, key.idCliente)
    }

    @Test
    fun `testa falha busca nao encontra chave`() {
        val idCliente = UUID.randomUUID().toString()
        val idPix = Random.nextLong()

        BDDMockito.given(rpc.get(Mockito.any())).willThrow(Status.NOT_FOUND.asRuntimeException())

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking().retrieve("/api/clientes/$idCliente/pix/$idPix")
        }

        assertEquals(HttpStatus.NOT_FOUND.code, erro.status.code)
    }

    @Test
    fun `testa falha busca chave sem permissao`() {
        val idCliente = UUID.randomUUID().toString()
        val idPix = Random.nextLong()

        BDDMockito.given(rpc.get(Mockito.any())).willThrow(Status.PERMISSION_DENIED.asRuntimeException())

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking().retrieve("/api/clientes/$idCliente/pix/$idPix")
        }

        assertEquals(HttpStatus.FORBIDDEN.code, erro.status.code)
    }

    @Test
    fun `testa busca lista de chaves vazia`() {
        val idCliente = UUID.randomUUID().toString()
        val response = ListKeyResponse.newBuilder().build()
        BDDMockito.given(rpc.list(Mockito.any())).willReturn(response)

        val result = http.toBlocking().retrieve("/api/clientes/$idCliente/pix")
        val keyList = mapper.readValue(result, mutableListOf<Chave>().javaClass)

        assertEquals(0, keyList.size)
    }

    @Test
    fun `testa busca lista de duas chaves`() {
        val idCliente = UUID.randomUUID().toString()

        val responseBuilder = GetKeyResponse.newBuilder()
            .setChave(UUID.randomUUID().toString())
            .setCriadaEm(LocalDateTime.now().toString())
            .setIdCliente(idCliente)
            .setIdPix(Random.nextLong())
            .setTipoChave(TipoChave.RANDOM)
            .setTitular(
                GetKeyResponse.Titular.newBuilder()
                    .setNome("Nome")
                    .setCpf("CPF")
                    .build()
            ).setInstituicao(
                GetKeyResponse.Instituicao.newBuilder()
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .setNome("Instituição")
                    .setConta("Conta")
                    .setAgencia("Agencia")
                    .build()
            )

        val chave1 = responseBuilder.build()

        val chave2 = responseBuilder
            .setChave(UUID.randomUUID().toString())
            .setCriadaEm(LocalDateTime.now().toString())
            .setIdPix(Random.nextLong())
            .build()

        val response = ListKeyResponse.newBuilder().addAllChaves(listOf(chave1, chave2)).build()

        BDDMockito.given(rpc.list(Mockito.any())).willReturn(response)

        val result = http.toBlocking().retrieve("/api/clientes/$idCliente/pix")
        val typeref = object : TypeReference<List<Chave>>() {}
        val keyList: List<Chave> = mapper.readValue(result, typeref)

        assertEquals(2, keyList.size)
        assertEquals(1, keyList.filter { it.idPix == chave1.idPix }.size)
        assertEquals(1, keyList.filter { it.idPix == chave2.idPix }.size)
    }

    @Test
    fun `testa falha busca cliente inexistente`() {
        val idCliente = UUID.randomUUID().toString()

        BDDMockito.given(rpc.list(Mockito.any())).willThrow(Status.NOT_FOUND.asRuntimeException())

        val erro = assertThrows(HttpClientResponseException::class.java) {
            http.toBlocking().retrieve("/api/clientes/$idCliente/pix")
        }

        assertEquals(HttpStatus.NOT_FOUND.code, erro.status.code)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockFactory {
        @Singleton
        fun rpc() = Mockito.mock(KeymanagerGRPCServiceGrpc.KeymanagerGRPCServiceBlockingStub::class.java)
    }
}