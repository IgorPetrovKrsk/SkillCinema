package igor.petrov.final_android_lvl1

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import igor.petrov.final_android_lvl1.data.AppDatabase
import igor.petrov.final_android_lvl1.data.dao.CollectionsDBDao
import igor.petrov.final_android_lvl1.data.dao.FilmDao
import igor.petrov.final_android_lvl1.data.dao.PersonDao
import igor.petrov.final_android_lvl1.data.repository.KinopoiskRepository
import igor.petrov.final_android_lvl1.data.repository.RoomDBRepository
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class KinopoiskUseCaseTest {

    private lateinit var database: AppDatabase
    private lateinit var collectionsDBDao: CollectionsDBDao
    private lateinit var filmDao: FilmDao
    private lateinit var personDao: PersonDao

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setupDatabaseAndDispatcher() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        collectionsDBDao = database.collectionsDBDao()
        filmDao = database.filmDao()
        personDao = database.personDao()

    }

    @Test
    fun getPerson() {
        runTest {
            val person = KinopoiskUseCase(KinopoiskRepository(),RoomDBRepository(collectionsDBDao,filmDao,personDao)).getPerson(66539)
            assertEquals(66539,person.personId)
            assertEquals("Vince Gilligan",person.nameEn)
            assertEquals("1967-02-10",person.birthday)
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun closeDatabaseAndDispatcher() {
        Dispatchers.resetMain()
        database.close()
    }
}