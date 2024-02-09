package igor.petrov.final_android_lvl1.data.dto.collectionDto

import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.entity.collectionDB.ItemCollectionDB

class ItemCollectionForRecyclerView(
    override val collectionId: Int,
    override val itemType: ItemCollectionType,
    override val itemId: Int,
    override val timeUpdated: Long,
    var posterUrl:String? = null,
    var itemName:String? = null,
    var itemDescription: String? = null,
    var itemRating: Float? = null
):ItemCollectionDB