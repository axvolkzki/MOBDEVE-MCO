package com.mobdeve.s14.abenoja_delacruz.bookcol.utils

import android.content.Context


class SessionManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("BookColPrefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    companion object {
        private const val SESSION_DURATION = 30L * 24 * 60 * 60 * 1000 // 30 days in milliseconds
        private const val KEY_SESSION_EXPIRY = "session_expiry"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
    }

    fun createLoginSession(userId: String, email: String) {
        editor.apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putLong(KEY_SESSION_EXPIRY, System.currentTimeMillis() + SESSION_DURATION)
            apply()
        }
    }

    fun clearSession() {
        editor.apply {
            clear()
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        val sessionExpiry = sharedPreferences.getLong(KEY_SESSION_EXPIRY, 0)
        return System.currentTimeMillis() < sessionExpiry
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }
}