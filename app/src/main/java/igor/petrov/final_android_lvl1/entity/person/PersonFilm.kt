package igor.petrov.final_android_lvl1.entity.person

interface PersonFilm {
    val filmId:Int
    val nameRu:String?
    val nameEng:String?
    val rating:String?
    val general:Boolean?
    val description:String?
    val professionKey:String?
    val posterUrl: String?
    val posterUrlPreview: String?
    val ratingKinopoisk: Float?
    val ratingImdb: Float?
}