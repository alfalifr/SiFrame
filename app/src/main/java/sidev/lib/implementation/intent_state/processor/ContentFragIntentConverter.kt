package sidev.lib.implementation.intent_state.processor

import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.intent_state.ContentFragIntent
import sidev.lib.implementation.presenter.ContentPresenter
import java.lang.reflect.Field

class ContentFragIntentConverter(view: ExpirableBase?, presenter: Presenter?)
    : IntentConverter<ContentFragIntent>(view, presenter){
    override fun getIntentDataPair(intent: ContentFragIntent, field: Field): Pair<String, Any>? {
        loge("field.name= ${field.name}")
        return if(intent is ContentFragIntent.Login && field.name == "uname") {
            loge("field.name= ${field.name} MASUK!!!")
            Pair(ContentPresenter.DATA_UNAME, intent.uname)
        }
            else super.getIntentDataPair(intent, field)
    }
}