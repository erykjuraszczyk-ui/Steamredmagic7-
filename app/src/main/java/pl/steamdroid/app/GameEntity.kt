package pl.steamdroid.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class GameLaunchMode {
    PC_STREAMING,
    LOCAL
}

@Entity(
    tableName = "games",
    indices = [Index(value = ["steamAppId"])]
)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val steamAppId: String? = null,
    val launchMode: String = GameLaunchMode.PC_STREAMING.name,
    val notes: String = "",
    val isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis(),
    val storeName: String? = null,
    val shortDescription: String? = null,
    val headerImageUrl: String? = null,
    @ColumnInfo(defaultValue = "''")
    val genres: String = "",
    @ColumnInfo(defaultValue = "''")
    val developers: String = "",
    val releaseDate: String? = null,
    @ColumnInfo(defaultValue = "0")
    val requiredAge: Int = 0,
    val metadataUpdatedAt: Long? = null
)
