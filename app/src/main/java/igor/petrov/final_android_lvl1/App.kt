package igor.petrov.final_android_lvl1

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import igor.petrov.final_android_lvl1.data.AppDatabase

@HiltAndroidApp
class App:Application() {

    lateinit var db: AppDatabase

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        val context: Context = App.applicationContext()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "final_android_lvl1_database"
        ).fallbackToDestructiveMigration().build()
    }
}