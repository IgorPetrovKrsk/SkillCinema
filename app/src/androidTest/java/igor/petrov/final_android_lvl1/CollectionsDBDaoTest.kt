package igor.petrov.final_android_lvl1

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import igor.petrov.final_android_lvl1.data.AppDatabase
import igor.petrov.final_android_lvl1.data.dao.CollectionsDBDao
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class CollectionsDBDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var collectionsDBDao: CollectionsDBDao
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        collectionsDBDao = database.collectionsDBDao()
    }

    @Test
    fun createNewCollectionTest()  {
        val collection = CollectionsDBDto("Test", true, 1)
        val collectionID = collectionsDBDao.newCollection(collection)
        assertEquals(1,collectionID)
    }

    @Test
    fun selectAllCollectionsTest(){
        val collection = CollectionsDBDto("Test", true, 1)
        collectionsDBDao.newCollection(collection)
        val collectionsList = collectionsDBDao.selectAll()
        assertEquals(collection, collectionsList[0])
    }

    @Test
    fun deleteCollectionTest(){
        val collection = CollectionsDBDto("Test", true, 1)
        collectionsDBDao.newCollection(collection)
        collectionsDBDao.deleteCollection(1)
        val collectionsList = collectionsDBDao.selectAll()
        assertEquals(0,collectionsList.size)
    }


    @After
    fun closeDatabase() {
        database.close()
    }
}