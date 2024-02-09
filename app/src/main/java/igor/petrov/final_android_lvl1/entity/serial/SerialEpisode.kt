package igor.petrov.final_android_lvl1.entity.serial

interface SerialEpisode {
    val seasonNumber: Int
    val episodeNumber: Int
    val nameRu: String?
    val nameEn: String?
    val synopsis: String?
    val releaseDate: String?


}