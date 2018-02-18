package ru.krygin.smart_sight.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import ru.krygin.smart_sight.R

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}