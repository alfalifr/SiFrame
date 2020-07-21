package sidev.lib.universal.annotation

import sidev.lib.universal.`fun`.callBySafely
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.isSuperclassOf

/** Implemtasi dari [AnnotatedFunctionClass], disertai cache. */
open class AnnotatedFunctionClassImpl: AnnotatedFunctionClass {
    private val annotatedFunCache: HashMap<Annotation, KFunction<*>>
            by lazy { HashMap<Annotation, KFunction<*>>() }

    final override fun <T : Annotation> callAnnotatedFunction(
        annotationClass: KClass<T>,
        checkFun: (T) -> Boolean,
        callFun: (KParameter) -> Any?
    ): KFunction<*>? {
        for(annotation in
            annotatedFunCache.keys.asSequence().filter { annotationClass.isSuperclassOf(it::class) }){
            if(checkFun(annotation as T)){
                val func= annotatedFunCache[annotation]!!
                val paramValMap= HashMap<KParameter, Any?>()
                for(param in func.parameters)
                    paramValMap[param]= if(param.kind == KParameter.Kind.VALUE) callFun(param) else this
                func.callBySafely(paramValMap)
                return func
            }
        }
        val newlyFoundFunc= super.callAnnotatedFunction(annotationClass, checkFun, callFun)
            ?: return null
        val newlyFoundAnnot= newlyFoundFunc.annotations.find { annotationClass.isSuperclassOf(it::class) }!! //Jika fungsinya ketemu, gak mungkin anotasinya null
        annotatedFunCache[newlyFoundAnnot]= newlyFoundFunc
        return newlyFoundFunc
    }

    final override fun <T : Annotation> callAnnotatedFunctionWithParamContainer(
        annotationClass: KClass<T>,
        paramContainer: Any?,
        checkFun: (T) -> Boolean
    ): KFunction<*>? {
        return super.callAnnotatedFunctionWithParamContainer(
            annotationClass,
            paramContainer,
            checkFun
        )
    }
}