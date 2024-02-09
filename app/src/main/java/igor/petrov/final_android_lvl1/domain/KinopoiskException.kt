package igor.petrov.final_android_lvl1.domain

class KinopoiskException(val code: Int, override val message: String?) : Throwable() {
}