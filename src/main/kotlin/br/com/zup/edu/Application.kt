package br.com.zup.edu

import io.micronaut.runtime.Micronaut.*
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
	info = Info(
		title = "Keymanager API",
		description = "PIX API para gerenciamento de chaves.",
		version = "1.0.0"
	)
)

object Application {
	@JvmStatic
	fun main(args: Array<String>) {
		build()
			.args(*args)
			.packages("br.com.zup.edu")
			.start()
	}
}

