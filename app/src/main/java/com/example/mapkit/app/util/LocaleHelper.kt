package com.example.mapkit.app.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.Locale

object LocaleHelper {

    const val LOCALE_UZ = "uz"
    const val LOCALE_UZ_CYRILLIC = "uz-rUZ"
    const val LOCALE_RU = "ru"
    const val LOCALE_EN = "en"
    const val LOCALE_KAA = "kaa"

    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"


/*
    fun contextWrapper(context: Context, language: String): ContextWrapper {
        var newContext = context

        val config = newContext.resources.configuration
        val sysLocale: Locale = config.locales[0]

        if (language != "" && !sysLocale.language.equals(language)) {
            setLocale(newContext, language)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            newContext = newContext.createConfigurationContext(config)
        } else {
            newContext.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
        return ContextWrapper(newContext)
    }
*/

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context)
        return setLocale(context, lang!!)
    }

    private fun persist(context: Context, language: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun getPersistedData(context: Context): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, LOCALE_UZ)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context)
    }

    fun setLocale(context: Context, language: String): Context {

        persist(context, language)

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
         }

          return  updateResourcesLegacy(context, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        /** val locale = Locale(language, "UZ")
        Locale.setDefault(locale) */

        val locale: Locale = when(language){
            LOCALE_UZ_CYRILLIC -> {
                Locale("uz", "UZ")
            }

            else -> {
                Locale(language)
            }
        }
        Locale.setDefault(locale)

        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(Locale.getDefault())
        configuration.setLayoutDirection(Locale.getDefault())
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale: Locale = when(language){
            LOCALE_UZ_CYRILLIC -> {
                Locale("uz", "UZ")
            }

            else -> {
                Locale(language)
            }
        }
        Locale.setDefault(locale)

        val resources: Resources = context.resources
        val configuration = Configuration()
        configuration.locale = Locale.getDefault()
        configuration.setLayoutDirection(Locale.getDefault())
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}
