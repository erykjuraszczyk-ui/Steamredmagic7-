package pl.steamdroid.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SteamDroidDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: SteamDroidDatabase? = null

        fun getInstance(context: Context): SteamDroidDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SteamDroidDatabase::class.java,
                    "steam_droid_library.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
