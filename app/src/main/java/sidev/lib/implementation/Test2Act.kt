package sidev.lib.implementation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsAct

class Test2Act : SimpleAbsAct() {
    override val layoutId: Int
        get() = R.layout.act_test2

    override fun initView(layoutView: View) {}
}
