package igor.petrov.final_android_lvl1.data

sealed class SearchState
 {
    object Ready : SearchState()
    object Disabled : SearchState()
    object Searching : SearchState()
}