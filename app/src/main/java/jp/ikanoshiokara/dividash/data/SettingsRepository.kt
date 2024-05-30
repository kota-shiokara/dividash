package jp.ikanoshiokara.dividash.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface SettingsRepository {
    companion object {
        val DEFAULT_USER_SETTINGS = UserSettings()
    }

    val runningTime: Flow<Int>
    val intervalTime: Flow<Int>
    val isAutoStart: Flow<Boolean>
    val userSettings: Flow<UserSettings>

    suspend fun saveRunningTime(runningTime: Int)

    suspend fun saveIntervalTime(intervalTime: Int)

    suspend fun saveAutoStart(isAutoStart: Boolean)
}

data class UserSettings(
    val runningTime: Int = -1,
    val intervalTime: Int = -1,
    val isAutoStart: Boolean = false,
)

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {
    override val runningTime =
        dataStore.data
            .catch { Log.e("${it.cause}", "${it.stackTrace}") }
            .map { preferences -> preferences[RUNNING_TIME] ?: (25 * 60) }

    override val intervalTime =
        dataStore.data
            .catch { Log.e("${it.cause}", "${it.stackTrace}") }
            .map { preferences -> preferences[INTERVAL_TIME] ?: (5 * 60) }

    override val isAutoStart =
        dataStore.data
            .catch { Log.e("${it.cause}", "${it.stackTrace}") }
            .map { preferences ->
                preferences[IS_AUTO_START] ?: false
            }

    override val userSettings =
        dataStore.data
            .catch { Log.e("${it.cause}", "${it.stackTrace}") }
            .map { preferences ->
                UserSettings(
                    runningTime = preferences[RUNNING_TIME] ?: (25 * 60),
                    intervalTime = preferences[INTERVAL_TIME] ?: (5 * 60),
                    isAutoStart = preferences[IS_AUTO_START] ?: false,
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

    override suspend fun saveAutoStart(isAutoStart: Boolean) {
        dataStore.edit {
            it[IS_AUTO_START] = isAutoStart
        }
    }
}
