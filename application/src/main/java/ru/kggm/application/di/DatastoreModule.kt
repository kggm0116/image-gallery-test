package ru.kggm.application.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.kggm.data.UserPreferencesData
import ru.kggm.data.datastore.UserPreferencesDataSerializer
import ru.kggm.data.repositories.UserPreferencesRepositoryImpl
import ru.kggm.domain.repositories.UserPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context): DataStore<UserPreferencesData> =
        DataStoreFactory.create(
            serializer = UserPreferencesDataSerializer,
            corruptionHandler = null,
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + Job()),
            produceFile = { context.dataStoreFile("USER_PREFERENCES") }
        )

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<UserPreferencesData>): UserPreferencesRepository =
        UserPreferencesRepositoryImpl(dataStore)
}