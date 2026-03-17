package com.tokama.linkmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val languagePreference = findPreference<ListPreference>(KEY_APP_LANGUAGE)
        val themePreference = findPreference<ListPreference>(AppUiSettings.KEY_THEME_MODE)
        val buildVersionPreference = findPreference<Preference>(KEY_BUILD_VERSION)

        languagePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        themePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        val currentLanguageValue = AppUiSettings.resolveCurrentAppLanguageValue(requireContext())
        if (languagePreference != null && languagePreference.value != currentLanguageValue) {
            languagePreference.value = currentLanguageValue
        }

        val currentThemeValue = AppUiSettings.getStoredThemeValue(requireContext())
        if (themePreference != null && themePreference.value != currentThemeValue) {
            themePreference.value = currentThemeValue
        }

        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            val selectedLanguage = AppUiSettings.normalizeLanguageValue(newValue as? String)
            AppCompatDelegate.setApplicationLocales(AppUiSettings.buildLocaleList(selectedLanguage))
            true
        }

        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            val selectedTheme = AppUiSettings.normalizeThemeValue(newValue as? String)
            AppCompatDelegate.setDefaultNightMode(AppUiSettings.toNightMode(selectedTheme))
            true
        }

        buildVersionPreference?.summary = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    }

    private companion object {
        const val KEY_APP_LANGUAGE = "app_language"
        const val KEY_BUILD_VERSION = "build_version"
    }
}
