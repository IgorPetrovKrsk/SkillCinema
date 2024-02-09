package igor.petrov.final_android_lvl1.data

sealed class DBUpdateState {
    object Ready : DBUpdateState()
    object Updating : DBUpdateState()
}