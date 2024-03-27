package igor.petrov.final_android_lvl1

import igor.petrov.final_android_lvl1.data.KinopoiskCollectionsRetrofit
import org.junit.Test
import kotlin.test.assertEquals

class RetrofitClientTest {
    @Test
    fun testRetrofitInstance() {
        val retrofitClient = KinopoiskCollectionsRetrofit.retrofit
        assertEquals(retrofitClient.baseUrl().toString(),"https://kinopoiskapiunofficial.tech/api/")
    }
}