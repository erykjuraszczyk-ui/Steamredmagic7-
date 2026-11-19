package pl.steamdroid.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [GameEntity::class],
    version = 3,
    exportSchema = false
)
abstract class SteamDroidDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE games ADD COLUMN storeName TEXT")
                database.execSQL("ALTER TABLE games ADD COLUMN shortDescription TEXT")
                database.execSQL("ALTER TABLE games ADD COLUMN headerImageUrl TEXT")
                database.execSQL("ALTER TABLE games ADD COLUMN genres TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE games ADD COLUMN developers TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE games ADD COLUMN releaseDate TEXT")
                database.execSQL("ALTER TABLE games ADD COLUMN requiredAge INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE games ADD COLUMN metadataUpdatedAt INTEGER")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE games ADD COLUMN localCompatibility TEXT NOT NULL DEFAULT 'NOT_TESTED'"
                )
                database.execSQL(
                    "ALTER TABLE games ADD COLUMN localRuntimePreset TEXT NOT NULL DEFAULT 'BALANCED'"
                )
                database.execSQL(
                    "ALTER TABLE games ADD COLUMN localTestNotes TEXT NOT NULL DEFAULT ''"
                )
                database.execSQL("ALTER TABLE games ADD COLUMN localTestedAt INTEGER")
            }
        }

        @Volatile
        private var INSTANCE: SteamDroidDatabase? = null

        fun getInstance(context: Context): SteamDroidDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SteamDroidDatabase::class.java,
                    "steam_droid_library.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
