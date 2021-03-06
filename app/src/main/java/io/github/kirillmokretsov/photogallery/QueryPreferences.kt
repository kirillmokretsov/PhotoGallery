package io.github.kirillmokretsov.photogallery

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val PREF_LAST_RESULT_ID = "lastResultId"
private const val PREF_IS_POLLING = "isPoling"

object QueryPreferences {

    fun getLastResultId(context: Context): String =
        PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LAST_RESULT_ID, "")!!

    fun setLastResultId(context: Context, lastResultId: String) =
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString(PREF_LAST_RESULT_ID, lastResultId)
        }

    fun isPoling(context: Context): Boolean =
        PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            PREF_IS_POLLING, false
        )

    fun setPoling(context: Context, isOn: Boolean) =
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putBoolean(PREF_IS_POLLING, isOn)
        }
}