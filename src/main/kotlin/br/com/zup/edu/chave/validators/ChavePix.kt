package br.com.zup.edu.chave.validators

import br.com.zup.edu.chave.CriaChaveRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Constraint
import kotlin.reflect.KClass

@Constraint(validatedBy = [ ChavePixValidator::class ])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChavePix(
    val message: String = "Chave PIX de tipo \${validatedValue.tipoChave} inválida.",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = []
)

@Singleton
class ChavePixValidator(@Inject val validators: Collection<PixValidator>):
    ConstraintValidator<ChavePix, CriaChaveRequest> {

    override fun isValid(
        value: CriaChaveRequest,
        annotationMetadata: AnnotationValue<ChavePix>,
        context: ConstraintValidatorContext
    ): Boolean {
        validators.forEach {
            if (it.supports(value.tipoChave) && !it.validate(value)) {
                return false
            }
        }
        return true
    }
}
