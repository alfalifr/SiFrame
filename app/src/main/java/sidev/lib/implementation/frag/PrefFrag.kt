package sidev.lib.implementation.frag

import android.app.PendingIntent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.implementation.R

class PrefFrag: PreferenceFragmentCompat() {
    companion object{
        const val PREF_NAME = "PREF_FRAG"
    }
    private lateinit var switch: SwitchPreferenceCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Called during [.onCreate] to supply the preferences for this fragment.
     * Subclasses are expected to call [.setPreferenceScreen] either
     * directly or via helper methods such as [.addPreferencesFromResource].
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     * this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     * [PreferenceScreen] with this key.
     */

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = PREF_NAME
        setPreferencesFromResource(R.xml.pref_cob, rootKey)

        switch = findPreference("notif")!!
        switch.setOnPreferenceChangeListener { pref, newValue ->
            loge("pref= $pref, newValue= $newValue")
            //val fromShared=
            true
        }
        //initConfig()
    }
/*
    private fun initConfig(){
        val firstRun = Util.getSharedPref(requireContext())
            .getBoolean(Const.KEY_FIRST_RUN, true)
        if(firstRun){
            AlarmNotifReceiver.setOn(requireContext())
            switch.isChecked = true
            Util.getSharedPref(requireContext()).edit {
                putBoolean(Const.KEY_FIRST_RUN, false)
            }
        } else {
            // If user installs update, but previously the preference was on, then just setOn the alarm.
            if(switch.isChecked
                && AlarmNotifReceiver.getAlarmPendingIntent(requireContext(), PendingIntent.FLAG_NO_CREATE) == null
            ){
                AlarmNotifReceiver.setOn(requireContext())
            }
        }
    }
 */
}