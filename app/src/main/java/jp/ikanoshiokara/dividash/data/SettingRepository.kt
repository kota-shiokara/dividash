package jp.ikanoshiokara.dividash.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface SettingRepository {
    val runningTime: Flow<Int>
    val intervalTime: Flow<Int>
    val userSettingTimes: Flow<UserSettingTimes>
    suspend fun saveRunningTime(runningTime: Int)
    suspend fun saveIntervalTime(intervalTime: Int)
}

data class UserSettingTimes(
    val runningTime: Int,
    val intervalTime: Int
)

class SettingRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): SettingRepository {

    override val runningTime = dataStore.data
        .catch { Log.e("${it.cause}", "${it.stackTrace}") }
        .map { preferences -> preferences[RUNNING_TIME] ?: (25 * 60) }

    override val intervalTime = dataStore.data
        .catch { Log.e("${it.cause}", "${it.stackTrace}") }
        .map { preferences -> preferences[INTERVAL_TIME] ?: (5 * 60) }

    override val userSettingTimes = dataStore.data
        .catch { Log.e("${it.cause}", "${it.stackTrace}") }
        .map { preferences ->
            UserSettingTimes(
                runningTime = preferences[RUNNING_TIME] ?: (25 * 60),
                intervalTime = preferences[INTERVAL_TIME] ?: (5 * 60)
            )
        }

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