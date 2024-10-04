package com.ivan.spaceflightnews.screens.search

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentSearchTermsStorage(private val context: Context) {

    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("recentSearchTerms")
        private val RECENT_SEARCHED_ITEMS_JSON_KEY = stringPreferencesKey("recent_searched_sites")
    }

    fun getRecentSearchTerms(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[RECENT_SEARCHED_ITEMS_JSON_KEY] ?: ""
        }

    suspend fun saveRecentSearchTerms(newJson: String) {
        context.dataStore.edit { preferences ->
            preferences[RECENT_SEARCHED_ITEMS_JSON_KEY] = newJson
        }
    }
}