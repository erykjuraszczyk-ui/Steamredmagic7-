package pl.steamdroid.app

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
    val addedAt: Long = System.currentTimeMillis()
)
