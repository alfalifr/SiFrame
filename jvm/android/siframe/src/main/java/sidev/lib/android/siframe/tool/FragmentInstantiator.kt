package sidev.lib.android.siframe.tool

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.forcedAddView
import sidev.lib.check.notNull
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * Kelas yg digunakan untuk mensimulasikan lifecycle pada fragment. Simulasi yg dilakukan oleh kelas ini
 * adalah simulasi tingkat rendah, karena tidak dapat menyimpan savedstate.
 * Kelas ini berguna untuk meng-commit [fragment] dg menggunakan [fragContainerView], jika tidak tersedia
 * [fragContainerId].
 */
open class FragmentInstantiator<F: Fragment> private constructor(
    val activity: ComponentActivity,
    var fragContainerView: ViewGroup? = null,
    val fragContainerId: Int = fragContainerView?.id ?: View.NO_ID,
    fragmentClass: KClass<F>
): LifecycleObserver {
    val fragment: F= fragmentClass.createInstance()

    class Builder<F: Fragment>(val activity: ComponentActivity, val fragmentClass: KClass<F>){
        fun commit(containerId: Int): FragmentInstantiator<F>
                = FragmentInstantiator(activity, fragContainerId = containerId, fragmentClass = fragmentClass)
        fun commit(containerView: ViewGroup): FragmentInstantiator<F>
                = FragmentInstantiator(activity, fragContainerView = containerView, fragmentClass = fragmentClass)
    }

    init{
        if(fragContainerView == null){
            try{
                fragContainerView= activity.findViewById(fragContainerId)!!
            } catch (e: KotlinNullPointerException){
                throw IllegalStateException("FragmentInstantiator untuk Fragment: \"$fragmentClass\" harus punya \"fragContainerView\" atau \"fragContainerId\" yg berada di dalam Activity: \"${activity::class}\"")
            }
        }
//        loge("fragment is Frag && activity is ActFragBase => ${fragment is Frag && activity is ActFragBase}")
        if(fragment is Frag && activity is ActFragBase)
            fragment.onLifecycleAttach(activity)
//            fragment._prop_parentLifecycle= activity

        activity.lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner){
        if(owner::class.qualifiedName != activity::class.qualifiedName) return
        fragment.onAttach(activity)
        fragment.onAttach(activity as Context)
//        fragment.onCreate(null)
        fragment.onCreateView(activity.layoutInflater, fragContainerView, null)
            .notNull {
                fragment.onViewCreated(it, null)
                fragContainerView!!.forcedAddView(it)
            }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(owner: LifecycleOwner){
        if(owner::class.qualifiedName != activity::class.qualifiedName) return
        fragment.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(owner: LifecycleOwner){
        if(owner::class.qualifiedName != activity::class.qualifiedName) return
        fragment.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(owner: LifecycleOwner){
        if(owner::class.qualifiedName != activity::class.qualifiedName) return
        fragment.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(owner: LifecycleOwner){
        if(owner::class.qualifiedName != activity::class.qualifiedName) return
        fragment.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner){
        if(owner::class.qualifiedName != activity::class.qualifiedName) return
        fragment.onDestroy()
        fragment.onDetach()
    }
}