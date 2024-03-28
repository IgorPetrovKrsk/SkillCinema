package igor.petrov.final_android_lvl1.data

import igor.petrov.final_android_lvl1.data.api.KinopoiskApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//private val BASE_URL = App.applicationContext().getString(R.string.kinopoisk_collections_base_url)
private val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"

object KinopoiskCollectionsRetrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val kinopoiskApi: KinopoiskApi = retrofit.create(KinopoiskApi::class.java)
}