package jp.ikanoshiokara.dividash.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface SettingRepository {
    val runningTime: Flow<Int>
    val intervalTime: Flow<Int>
    suspend fun saveRunningTime(runningTime: Int)
    suspend fun saveIntervalTime(intervalTime: Int)
}

class SettingRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): SettingRepository {

    override val runningTime = dataStore.data
        .catch { Log.e("${it.cause}", "${it.stackTrace}") }
        .map { preferences -> preferences[RUNNING_TIME] ?: 25 }

    override val intervalTime = dataStore.data
        .catch { Log.e("${it.cause}", "${it.stackTrace}") }
        .map { preferences -> preferences[INTERVAL_TIME] ?: 5 }

    override suspend fun saveRunningTime(runningTime: Int) {
        dataStore.edit {
            it[RUNNING_TIME] = runningTime
        }
    }

    override suspend fun saveIntervalTime(intervalTime: Int) {
        dataStore.edit {
            it[INTERVAL_TIME] = intervalTime
        }
    }
}