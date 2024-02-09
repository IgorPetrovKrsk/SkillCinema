package igor.petrov.final_android_lvl1.entity.collectionDB

import igor.petrov.final_android_lvl1.data.ItemCollectionType

interface ItemCollectionDB {
    val collectionId: Int
    val itemType: ItemCollectionType
    val itemId: Int
    val timeUpdated: Long //System.currentTimeMillis()
}