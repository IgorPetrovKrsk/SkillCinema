package igor.petrov.final_android_lvl1.entity

interface PhotoList {
    val total:Int
    val totalPages: Int
    val items: List<Photo>?
}