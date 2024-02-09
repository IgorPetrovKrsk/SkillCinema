package igor.petrov.final_android_lvl1.entity

import igor.petrov.final_android_lvl1.data.FilmType

interface Film {
    val kinopoiskId: Int
    val kinopoiskHDId: String?
    val imdbId: String?
    val nameRu: String?
    val nameEn: String?
    val nameOriginal: String?
    val year:Int?
    val posterUrl:String?
    val posterUrlPreview:String?
    val coverUrl: String?
    val logoUrl: String?
    val ratingKinopoisk: Float?
    val ratingImdb: Float?
    val webUrl: String?
    val slogan: String?
    val description: String?
    val shortDescription: String?
    val countries : List<Country>?
    val genres: List<Genre>?
    val duration: Int?
    val premiereRu: String?
    val type: FilmType?
    val serial: Boolean?
    val shortFilm: Boolean?
    val completed: Boolean?
    val ratingMpaa: String?
    val ratingAgeLimits: String?
}