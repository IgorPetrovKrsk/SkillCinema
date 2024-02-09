package igor.petrov.final_android_lvl1.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import igor.petrov.final_android_lvl1.data.dao.CollectionsDBDao
import igor.petrov.final_android_lvl1.data.dao.FilmDao
import igor.petrov.final_android_lvl1.data.dao.PersonDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun collectionsDBDao(appDatabase: AppDatabase): CollectionsDBDao{
        return appDatabase.collectionsDBDao()
    }
    @Provides
    fun filmDao(appDatabase: AppDatabase): FilmDao{
        return appDatabase.filmDao()
    }
    @Provides
    fun personDao(appDatabase: AppDatabase): PersonDao{
        return appDatabase.personDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "final_android_lvl1_database"
        ).fallbackToDestructiveMigration().build()
    }
}
