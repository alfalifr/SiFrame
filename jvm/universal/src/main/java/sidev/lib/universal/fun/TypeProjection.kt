package sidev.lib.universal.`fun`

import sidev.lib.universal.exception.ImplementationExc
import sidev.lib.universal.structure.data.*
import sidev.lib.universal.structure.data.ProjectedTypeParameterImpl
import sidev.lib.universal.structure.data.asProjectedTypeParameter
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType



fun LinkedTypeParameter.asLinkedProjectedTypeParameter(): LinkedProjectedTypeParameter
        = LinkedProjectedTypeParameter(
    ProjectedTypeParameterImpl(typeParam),
    upperBoundTypeParam.map { ProjectedTypeParameterImpl(it) }
)


@ExperimentalStdlibApi
fun List<LinkedTypeParameter>.toLinkedProjectedTypeParameterList(): List<LinkedProjectedTypeParameter>{
    //1. Isi dulu list dg ProjectedTypeParameter.
    val projectedTypeParamList= ArrayList<ProjectedTypeParameterImpl>()
    for(i in this.indices)
        projectedTypeParamList += ProjectedTypeParameterImpl(this[i].typeParam)

    //2. Cari ProjectedUpperBoundTypeParameter yg sama dg ProjectedTypeParameter yg telah diisi di awal.
    // Pencarian ini bertujuan agar instance pada [LinkedProjectedTypeParameter.typeParam] sama dg instance pada [LinkedProjectedTypeParameter.upperBoundTypeParam].
    // Langkah ini bertujuan untuk mempermudah proses penyimpan KTypeProjection dari KTypeParameter
    // yg terdapat pada sebuah kelas, terutama saat terdapat nested KTypeParameter pada type param lainnya.
    val linkedProjectedTypeParameterList= ArrayList<LinkedProjectedTypeParameter>()
    for((i, projectedTypeParam) in projectedTypeParamList.withIndex()){
        val projectedUpperBoundTypeParamList= ArrayList<ProjectedTypeParameter>()
        for(upperBound in this[i].upperBoundTypeParam){
            projectedTypeParamList.find { it.typeParam == upperBound }
                .notNull { projectedUpperBoundTypeParamList += it }
        }
        linkedProjectedTypeParameterList += LinkedProjectedTypeParameter(projectedTypeParam, projectedUpperBoundTypeParamList)
    }
    return linkedProjectedTypeParameterList
}

@ExperimentalStdlibApi
val KClass<*>.linkedProjectedTypeParameters: List<LinkedProjectedTypeParameter>
    get(){
        val linkedProjectedTypeParamList= ArrayList<LinkedProjectedTypeParameter>()
        val typeParams= typeParameters
        for(typeParam in typeParams){
            val upperBoundTypeParams= ArrayList<ProjectedTypeParameter>()
            //Cek apakah [typeParam] punya upperBound yg melibatkan typeParam lain dalam satu kelas.
            for(upperBound in typeParam.upperBounds){
                for(arg in upperBound.arguments){
                    if(arg.type?.classifier in typeParams){
                        upperBoundTypeParams += (arg.type!!.classifier as KTypeParameter).asProjectedTypeParameter()
                    }
                }
            }
            linkedProjectedTypeParamList += LinkedProjectedTypeParameter(typeParam.asProjectedTypeParameter(), upperBoundTypeParams)
        }
        return linkedProjectedTypeParamList
    }


// */


/**
 * Mengambil [KTypeProjection] dari `this.extension` [KTypeParameter] dg [KType]
 * dari kelas sesungguhnya yg disimpulkan dari nilai properti yg dimiliki oleh [owner].
 */
@ExperimentalStdlibApi
fun ProjectedTypeParameter.getClassProjectionIn(owner: Any): Boolean {
    if(this !is ProjectedTypeParameterImpl)
        throw ImplementationExc(implementedClass = ProjectedTypeParameter::class, msg = "Tidak dapat memanggil getClassProjectionIn() dg kelas modifikasi.")

    for(prop in owner::class.declaredMemberPropertiesTree){
        if(this == prop.returnType.classifier)
            prop.forcedGet(owner).notNull { valueProjection ->
//                    prine("owner= $owner prop= $prop valueProjection= $valueProjection class= ${valueProjection::class}")
                val typeParamProjectionList= ArrayList<KTypeProjection>()
//                for(ownerTypeParam in owner::class.typeParameters)
                for(typeParam in valueProjection::class.typeParameters)
                    typeParam.getClassProjectionIn(valueProjection)
                        .notNull { typeParamProjectionList += it }
//                            .isNull { typeParamProjectionList += KTypeProjection.STAR }
                val type= valueProjection::class.createType(typeParamProjectionList, prop.returnType.isMarkedNullable)
                projection= KTypeProjection(typeParam.variance, type)
                return true
            }
    }
    prine("""ProjectedTypeParameter.getProjectionIn() -> Tidak bisa mendapatkan proyeksi dari ProjectedTypeParameter: "$this", proyeksi akhir "KTypeProjection.STAR".""")
    return false
}

/*
fun LinkedProjectedTypeParameter.getClassProjectionIn(owner: Any): Boolean{
    if(!typeParam.getClassProjectionIn(owner)) return false
    for((variance, type) in typeParam.typeParam.nestedUpperBoundTypeArguments){
        if(type?.classifier is KTypeParameter){

        }
    }
//    typeParam.projection.
}
 */


@ExperimentalStdlibApi
val KClass<*>.linkedTypeParameters: List<LinkedTypeParameter>
    get(){
        val linkedTypeParamList= ArrayList<LinkedTypeParameter>()
        val typeParams= typeParameters
        for(typeParam in typeParams){
            val upperBoundTypeParams= ArrayList<KTypeParameter>()
            for(upperBound in typeParam.upperBounds){
                for(arg in upperBound.arguments){
                    if(arg.type?.classifier in typeParams){
                        upperBoundTypeParams += arg.type!!.classifier as KTypeParameter
                    }
                }
            }
            linkedTypeParamList += LinkedTypeParameter(typeParam, upperBoundTypeParams)
        }
        return linkedTypeParamList
    }