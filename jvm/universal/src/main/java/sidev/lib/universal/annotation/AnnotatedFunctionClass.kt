package sidev.lib.universal.annotation

import sidev.lib.universal.`fun`.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.isSuperclassOf

interface AnnotatedFunctionClass {
    fun <T: Annotation> callAnnotatedFunction(
        annotationClass: KClass<T>,
        checkFun: ((T) -> Boolean) = {true},
        callFun: (KParameter) -> Any?
    ): KFunction<*>? {
        var foundAnnotation: T?
        for(func in this::class.declaredMemberFunctions){
            if(func.annotations.find { annotationClass.isSuperclassOf(it::class) }.also { foundAnnotation= it as? T } != null
                && checkFun(foundAnnotation!!)){

                val paramValMap= HashMap<KParameter, Any?>()
                for(param in func.parameters){
//                    prine("Annot param= $param")
                    paramValMap[param]= if(param.kind == KParameter.Kind.VALUE) callFun(param) else this
                }
                func.callBySafely(paramValMap)
                return func
            }
        }
        return null
    }

    /**
     * Mirip dg [callAnnotatedFunction], namun parameter diperoleh dari [paramContainer] scr langsung.
     * Perlu diperhatikan bahwa nilai yg diambil dari [paramContainer] adalah properti di dalamnya.
     * Jika [paramContainer] berupa array atau collection, maka nilai yg diambil bkn nilai yg ada di dalamnya,
     * melainkan properti di dalamnya, seperti [Array.size] atau [Collection.size].
     *
     * Fungsi ini mengambil nilai dari properti di dalam [paramContainer] dg cara mencocokan nama
     * properti dan parameter yg sama. Jika ada perubahan nama menggunakan anotasi [Rename],
     * maka nama tersebut yg diambil.
     */
    fun <T: Annotation> callAnnotatedFunctionWithParamContainer(
        annotationClass: KClass<T>,
        paramContainer: Any?,
        checkFun: ((T) -> Boolean) = {true}
    ): KFunction<*>? {
        return callAnnotatedFunction(annotationClass, checkFun){ param ->
            if(paramContainer != null){
                var value: Any?= null
                for(valMap in paramContainer.implementedAccesiblePropertiesValueMapTree){
                    if(valMap.first.renamedName == param.renamedName){
                        value= valMap.second
                        break
                    }
                }
                value
            } else null
        }
    }
}