package sidev.lib.android.siframe.intfc.`fun`

interface InitPropFun {
    var isInit: Boolean
    fun initProp(forceInit: Boolean= false, init: () -> Unit){
        if(!isInit || forceInit){
            init()
            isInit= true
        }
    }
}