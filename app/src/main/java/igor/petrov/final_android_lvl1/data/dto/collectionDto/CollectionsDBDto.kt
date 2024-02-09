package igor.petrov.final_android_lvl1.data.dto.collectionDto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB

@Entity(tableName = "CollectionsDB")
data class CollectionsDBDto(
    @ColumnInfo(name = "collectionName")
    override val collectionName: String,
    @ColumnInfo(name = "buildIn")
    override val buildIn: Boolean,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collectionId")
    override val collectionId: Int = 0,
    override val collectionSize: Int = 0
    ):CollectionsDB
