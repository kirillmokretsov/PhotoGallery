package io.github.kirillmokretsov.photogallery

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val PREF_STORED_QUERY = "storedQuery"
private const val PREF_LAST_RESULT_ID = "lastResultId"

object QueryPreferences {
    fun getStoredQuery(context: Context): String =
        PreferenceManager.getDefaultSharedPreferences(context).getString(
            PREF_STORED_QUERY, ""
        )!!
    fun setStoredQuery(context: Context, queryString: String) =
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString(PREF_STORED_QUERY, queryString)
        }

    fun getLastResultId(context: Context): String =
        PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LAST_RESULT_ID, "")!!

    fun setLastResultId(context: Context, lastResultId: String) =
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString(PREF_LAST_RESULT_ID, lastResultId)
        }
}