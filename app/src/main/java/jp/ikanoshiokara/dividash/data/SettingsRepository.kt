package jp.ikanoshiokara.dividash.data

import android.content.Context
import android.media.RingtoneManager
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
    val ringtoneType: Flow<Int>
    val userSettings: Flow<UserSettings>

    suspend fun saveRunningTime(runningTime: Int)

    suspend fun saveIntervalTime(intervalTime: Int)

    suspend fun saveAutoStart(isAutoStart: Boolean)

    suspend fun saveRingtoneType(ringtoneType: Int)

    fun getRingtoneList(context: Context): List<RingtoneInfo>
}

data class UserSettings(
    val runningTime: Int = -1,
    val intervalTime: Int = -1,
    val isAutoStart: Boolean = false,
    val ringtoneType: Int = -1
)

data class RingtoneInfo(
    val title: String,
    val index: Int
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

    override val ringtoneType =
        dataStore.data
            .catch { Log.e("${it.cause}", "${it.stackTrace}") }
            .map { preferences ->
                preferences[RINGTONE_TYPE] ?: -1
            }

    override val userSettings =
        dataStore.data
            .catch { Log.e("${it.cause}", "${it.stackTrace}") }
            .map { preferences ->
                UserSettings(
                    runningTime = preferences[RUNNING_TIME] ?: (25 * 60),
                    intervalTime = preferences[INTERVAL_TIME] ?: (5 * 60),
                    isAutoStart = preferences[IS_AUTO_START] ?: false,
                    ringtoneType = preferences[RINGTONE_TYPE] ?: -1
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

    override suspend fun saveRingtoneType(ringtoneType: Int) {
        dataStore.edit {
            it[RINGTONE_TYPE] = ringtoneType
        }
    }

    override fun getRingtoneList(context: Context): List<RingtoneInfo> {
        val ringtoneList = mutableListOf<RingtoneInfo>()

        val manager = RingtoneManager(context)
        manager.setType(RingtoneManager.TYPE_ALL)
        val cursor = manager.cursor
        while (cursor.moveToNext()) {
            val index = cursor.getInt(RingtoneManager.ID_COLUMN_INDEX)
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)

            ringtoneList.add(
                RingtoneInfo(
                    title = title,
                    index = index
                )
            )
        }

        return ringtoneList
    }
}
