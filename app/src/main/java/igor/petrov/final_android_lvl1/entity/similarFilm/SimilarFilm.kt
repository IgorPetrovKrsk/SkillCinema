package igor.petrov.final_android_lvl1.entity.similarFilm

interface SimilarFilm {
    val filmId: Int
    val nameRu: String?
    val nameEn: String?
    val nameOriginal: String?
    val posterUrl:String?
    val posterUrlPreview:String?
    val relationType:String?
}