package sidev.lib.universal.intfc

import sidev.lib.universal.`fun`.*
import kotlin.collections.filter
import kotlin.reflect.KType

/** Callable sederhana yg mengembalikan (return) [R]. */
interface SifCallable<out R>: Function<R> {
    fun callBy(vararg args: Pair<SifParameter, Any?>): R
}

/** Fungsi sederhana yg mengembalikan (return) [R] yg disetai dg informasi terkait parameter. */
interface SifFunction<out R>: SifCallable<R> {
    val parameters: List<SifParameter>
    val name: String
//    val returnType: KType
    fun callPositionedBy(vararg args: Any?): R
    fun callNamedBy(vararg args: Pair<String, Any?>): R
    fun getDefaultValue(parameter: SifParameter): Any?
}

fun <R> createFunction(
    name: String, parameters: List<SifParameter> = emptyList(),
    defaultValueFunc: (SifParameter) -> Any? = { null },
    callBlock: (args: Map<SifParameter, Any?>) -> R
): SifFunction<R>
    = object : SifFunctionImpl<R>(){
    override val parameters: List<SifParameter> = parameters
    override val name: String = name
    override fun getDefaultValue(parameter: SifParameter): Any? = defaultValueFunc(parameter)
    override fun callBlock(args: Map<SifParameter, Any?>): R = callBlock(args)
}

internal abstract class SifFunctionImpl<out R>: SifFunction<R>{
    val requiredParameterCount: Int by lazy { parameters.filter { !it.isOptional }.size }

    abstract fun callBlock(args: Map<SifParameter, Any?>): R

    override fun callBy(vararg args: Pair<SifParameter, Any?>): R {
        evaluateRequiredArgs(*args)
        val passedParams= args.firstList intersect parameters
        val passedArgVals= passedParams.mapIndexed { i, sifParameter -> args[i].second }

        for((i, param) in passedParams.withIndex()){
            if(!param.type.isAssignableFrom(passedArgVals[i]))
                throw IllegalArgumentException("""$this callBy() -> tipe param: "${param.name}: ${param.type}" tidak kompatibel dg argumen: "${passedArgVals[i]}".""")
        }
        return callBlock(passedParams.asMapWithValues(passedArgVals))
    }

    override fun callPositionedBy(vararg args: Any?): R
            = callBy(*args.mapIndexed { i, arg -> Pair(parameters[i], arg) }.toTypedArray())

    override fun callNamedBy(vararg args: Pair<String, Any?>): R {
        val passedArgs= args.firstList.mapIndexedNotNull { i, passedName ->
            parameters.find { it.name ==  passedName }.notNullTo { Pair(it, args[i].second) }
        }.toTypedArray()
        return callBy(*passedArgs)
    }

    private fun evaluateRequiredArgs(vararg args: Pair<SifParameter, Any?>){
        val missedParams= parameters.asSequence() - args.firstList
        for(param in missedParams){
            if(!param.isOptional){
                val validProvidedArgCount= (parameters intersect args.firstList).size
                throw IllegalArgumentException("$this callBy() -> harus menerima $requiredParameterCount argumen, namun tersedia $validProvidedArgCount")
            }
        }
    }

    override fun toString(): String = """Fungsi: "$name"."""
}

/** Parameter yg digunakan pada [SifFunction]. */
interface SifParameter{
    val index: Int
    val name: String
    val type: KType
    val isOptional: Boolean
}

fun createParameter(type: KType, index: Int= 0, name: String= "<param>", isOptional: Boolean= false): SifParameter
    = object : SifParameter{
        override val index: Int= index
        override val name: String= name
        override val type: KType= type
        override val isOptional: Boolean= isOptional
    }

fun createParameterList(vararg paramInfo: Pair<String, KType>, defaultIsOptional: Boolean= false): List<SifParameter>
    = paramInfo.mapIndexed { i, info ->
        createParameter(info.second, i, info.first, defaultIsOptional)
    }


/**
 * Interface yg memanajemen bbrp listener dg tipe [L] yg memiliki tipe return [Rf].
 * Interface ini juga dapat menghasilkan kembalian (return) dg tipe [Rl].
 */
interface ListenerManager_<Rf, Rl>{
    val listeners: Map<String, SifCallable<Rf>>

    /**
     * @return -> `true` jika [l] dg [tag] berhasil ditambahkan ke [listeners],
     *   -> `false` jika ternyata ada listener lain dg [tag] tg sama dan [forceReplaceWithSameTag] == false.
     */
    fun addListener(l: SifCallable<Rf>, tag: String = listeners.size.toString(), forceReplaceWithSameTag: Boolean = true): Boolean

    /** Bentuk lain dari [addListener] sehingga bentuk SAM constructor dapat dilakukan dg mudah. */
    fun addListener(tag: String = listeners.size.toString(), forceReplaceWithSameTag: Boolean = true, l: (args: Array<out Pair<SifParameter, Any?>>) -> Rf): Boolean

    /** @return `true` jika [l] berhasil dihapus dari [listeners], `false` jika [l] tidak ada pada [listeners]. */
    fun removeListener(l: SifCallable<Rf>): Boolean

    /** @return [L] dg [tag] yg dihapus dari [listeners], `null` jika tidak ada listener dg [tag] pada [listeners]. */
    fun removeListenerByTag(tag: String): SifCallable<Rf>?

    /** Memanggil semua listener pada [listeners] dg argumen [args]. */
    fun iterateListeners(vararg args: Pair<SifParameter, Any?>): Rl

    /** Memproses semua output listener setelah dipanggil dari [iterateListeners] berupa [listenerOutput] menjadi [Rl]. */
    fun processOutput(listenerOutput: Map<String, Rf>): Rl
}

internal abstract class ListenerManagerImpl<Rf, Rl>: ListenerManager_<Rf, Rl>{
    final override val listeners: Map<String, SifCallable<Rf>> = HashMap()
    private val castedListenersMap: HashMap<String, SifCallable<Rf>>
        get()= listeners as HashMap<String, SifCallable<Rf>>

    override fun addListener(l: SifCallable<Rf>, tag: String, forceReplaceWithSameTag: Boolean): Boolean {
        return if(forceReplaceWithSameTag || tag in castedListenersMap.keys){
            castedListenersMap[tag]= l
            true
        } else false
    }

    override fun addListener(
        tag: String,
        forceReplaceWithSameTag: Boolean,
        l: (args: Array<out Pair<SifParameter, Any?>>) -> Rf
    ): Boolean = addListener(object : SifCallable<Rf>{
        override fun callBy(vararg args: Pair<SifParameter, Any?>): Rf = l(args)
    }, tag, forceReplaceWithSameTag)
/*
    override fun addListener(tag: String, forceReplaceWithSameTag: Boolean, l: SifCallable<Rf>): Boolean
            = addListener(l, tag, forceReplaceWithSameTag)
 */

    override fun removeListener(l: SifCallable<Rf>): Boolean = castedListenersMap.removeValue(l)
    override fun removeListenerByTag(tag: String): SifCallable<Rf>? = castedListenersMap.remove(tag)

    override fun iterateListeners(vararg args: Pair<SifParameter, Any?>): Rl {
        val listenerOutput= listeners.map { Pair(it.key, it.value.callBy(*args)) }.asMap()
        return processOutput(listenerOutput)
    }
}

fun <Rf, Rl> createListener(processOutputFunc: (listenerOutput: Map<String, Rf>) -> Rl): ListenerManager_<Rf, Rl>
    = object : ListenerManagerImpl<Rf, Rl>(){
        override fun processOutput(listenerOutput: Map<String, Rf>): Rl = processOutputFunc(listenerOutput)
    }

fun  createSimpleListener_(): ListenerManager_<Unit, Unit>
        = object : ListenerManagerImpl<Unit, Unit>(){
            override fun processOutput(listenerOutput: Map<String, Unit>){}
        }
