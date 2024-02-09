package igor.petrov.final_android_lvl1.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto

@Dao
interface PersonDao {

    @Query("Select * FROM PersonDB WHERE personId = :personId")
    fun getPerson(personId:Int): PersonDto?

    @Upsert
    fun newPerson(newPerson: PersonDto)

    @Query ("DELETE FROM PersonDB WHERE personId = :personId")
    fun deletePerson(personId:Int)

    @Query ("DELETE FROM PersonDB")
    fun deleteAllPerson()
}