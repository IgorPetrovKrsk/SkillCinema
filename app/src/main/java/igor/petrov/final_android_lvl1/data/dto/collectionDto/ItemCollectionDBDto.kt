package igor.petrov.final_android_lvl1.data.dto.collectionDto

import androidx.room.ColumnInfo
import androidx.room.Entity
import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.entity.collectionDB.ItemCollectionDB

@Entity(tableName = "ItemCollectionDB",
    primaryKeys = ["collectionId", "itemType", "itemId"]
)
data class ItemCollectionDBDto(
    @ColumnInfo(name = "collectionId")
    override val collectionId: Int,
    @ColumnInfo(name = "itemType")
    override val itemType: ItemCollectionType,
    @ColumnInfo(name = "itemId")
    override val itemId: Int,
    @ColumnInfo(name = "timeUpdated")
    override val timeUpdated: Long = System.currentTimeMillis()
) : ItemCollectionDB
