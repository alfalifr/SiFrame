package sidev.lib.universal.annotation

import sidev.lib.universal.`fun`.prine
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation

/**
 * Digunakan untuk menandai bahwa element yg ditandai dg anotasi ini memiliki nama yg berbeda
 * sesuai kebutuhan penggunaan anotasi ini.
 */
//@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Rename(val newName: String)


val KAnnotatedElement.renamedName: String
    get(){
        return findAnnotation<Rename>()?.newName
            ?: when(this){
                is KParameter -> name ?: "<parameter: ${toString()}>"
                is KCallable<*> -> name
                is KClass<*> -> qualifiedName ?: "<class: ${toString()}>"
                is KType -> (classifier as? KClass<*>)?.qualifiedName ?: toString()
                is KTypeParameter -> name
                is KClassifier -> toString()
                is Annotation -> this::class.qualifiedName ?: "<annotation: ${toString()}>"
                else -> toString()
            }
    }