import android.util.Log
import androidx.datastore.core.DataStore
import com.example.imagegallerytest.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class UserPreferencesRepository(
    private val userPreferencesData: DataStore<UserPreferences>
) {
    val userPreferences: Flow<UserPreferences> = userPreferencesData.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("UserPreferencesRepo", "Error reading user preferences.", exception)
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateFoldersGridColumnsLandscape(value: Int) {
        userPreferencesData.updateData {
            it.toBuilder().setFoldersGridColumnsLandscape(value).build()
        }
    }

    suspend fun updateFoldersGridColumnsPortrait(value: Int) {
        userPreferencesData.updateData {
            it.toBuilder().setFoldersGridColumnsPortrait(value).build()
        }
    }

    suspend fun updateFilesGridColumnsLandscape(value: Int) {
        userPreferencesData.updateData {
            it.toBuilder().setFilesGridColumnsLandscape(value).build()
        }
    }

    suspend fun updateFilesGridColumnsPortrait(value: Int) {
        userPreferencesData.updateData {
            it.toBuilder().setFilesGridColumnsPortrait(value).build()
        }
    }
}