package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class IllegalStateExc(
    relatedClass: KClass<*>?= IllegalStateExc::class,
    stateOwner: KClass<*>? = null,
    currentState: Any?= "<currentState>",
    expectedState: Any?= "<expectedState>",
    detMsg: String= "")
    : Exc(relatedClass, "stateOwner: $stateOwner; currentState: $currentState; expectedState: $expectedState", detMsg)