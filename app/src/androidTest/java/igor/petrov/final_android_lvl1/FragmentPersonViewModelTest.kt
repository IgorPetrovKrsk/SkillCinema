package igor.petrov.final_android_lvl1

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import igor.petrov.final_android_lvl1.data.AppDatabase
import igor.petrov.final_android_lvl1.data.dao.CollectionsDBDao
import igor.petrov.final_android_lvl1.data.dao.FilmDao
import igor.petrov.final_android_lvl1.data.dao.PersonDao
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto
import igor.petrov.final_android_lvl1.data.repository.KinopoiskRepository
import igor.petrov.final_android_lvl1.data.repository.RoomDBRepository
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.presentation.ui.person.FragmentPersonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class FragmentPersonViewModelTest {

    private lateinit var database: AppDatabase
    private lateinit var collectionsDBDao: CollectionsDBDao
    private lateinit var filmDao:FilmDao
    private lateinit var personDao: PersonDao
    private lateinit var personViewModel: FragmentPersonViewModel

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
    fun getTestPerson() {
        personViewModel = FragmentPersonViewModel(KinopoiskUseCase(KinopoiskRepository(),RoomDBRepository(collectionsDBDao,filmDao,personDao)))
        personViewModel.personId = 66539 //Vince Gilligan
        runTest {
                personViewModel.getPerson()
        }
        assertEquals(personViewModel.person.value.personId,PersonDto(0).personId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun closeDatabaseAndDispatcher() {
        Dispatchers.resetMain()
        database.close()
    }
}