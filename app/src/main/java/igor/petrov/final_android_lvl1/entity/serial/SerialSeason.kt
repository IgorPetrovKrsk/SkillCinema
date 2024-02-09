package igor.petrov.final_android_lvl1.entity.serial

interface SerialSeason {
    val number:Int
    val episodesList: List<SerialEpisode>
}