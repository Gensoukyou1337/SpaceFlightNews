package com.ivan.spaceflightnews.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginDataStorage(private val context: Context) {
    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("loginData")
        private val LOGIN_ID_TOKEN = stringPreferencesKey("login_id_token")
        private val USER_NAME = stringPreferencesKey("user_name")
    }

    fun getLoginIDToken(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[LOGIN_ID_TOKEN] ?: ""
        }

    fun getUserName(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[USER_NAME] ?: ""
        }

    suspend fun saveLoginIDToken(newAccessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_ID_TOKEN] = newAccessToken
        }
    }

    suspend fun saveUserName(newUserName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = newUserName
        }
    }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_ID_TOKEN] = ""
            preferences[USER_NAME] = ""
        }
    }
}