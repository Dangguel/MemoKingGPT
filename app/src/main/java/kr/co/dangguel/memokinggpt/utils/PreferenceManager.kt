package kr.co.dangguel.memokinggpt.util

import android.content.Context

object PreferenceManager {
    private const val PREF_NAME = "memo_pref"
    private const val KEY_GPT_COUNT = "gpt_usage_count"

    fun getPrefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getSummaryCount(context: Context): Int {
        return getPrefs(context).getInt(KEY_GPT_COUNT, 0)
    }

    fun increaseSummaryCount(context: Context) {
        val count = getSummaryCount(context) + 1
        getPrefs(context).edit().putInt(KEY_GPT_COUNT, count).apply()
    }

    fun resetSummaryCount(context: Context) {
        getPrefs(context).edit().putInt(KEY_GPT_COUNT, 0).apply()
    }
}
