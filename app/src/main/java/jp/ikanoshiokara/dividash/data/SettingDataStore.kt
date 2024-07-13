package jp.ikanoshiokara.dividash.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.settingDataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

val RUNNING_TIME = intPreferencesKey("running")
val INTERVAL_TIME = intPreferencesKey("interval")
val IS_AUTO_START = booleanPreferencesKey("auto_start")
val RINGTONE_URI = stringPreferencesKey("ringtone_uri")
