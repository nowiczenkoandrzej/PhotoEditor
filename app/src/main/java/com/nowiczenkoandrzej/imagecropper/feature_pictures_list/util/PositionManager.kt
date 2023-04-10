package com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PositionManager(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "POSITION")
    private val dataStore = context.dataStore

    companion object {
        val position = intPreferencesKey("POSITION")
    }


    suspend fun setPosition(pos: Int) {
        dataStore.edit { pref ->
            pref[position] = pos
        }
    }

    fun getPosition(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if(exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                val pos = pref[position] ?: 0
                pos
            }
    }

}