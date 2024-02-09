package igor.petrov.final_android_lvl1.data

sealed class WelcomeState {
    object Welcome : WelcomeState()
    object Loading : WelcomeState()
    object Ready    : WelcomeState()
}