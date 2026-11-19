package pl.steamdroid.app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY isFavorite DESC, title COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<GameEntity>>

    @Query("SELECT COUNT(*) FROM games WHERE steamAppId = :appId")
    suspend fun countBySteamAppId(appId: String): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(game: GameEntity): Long

    @Update
    suspend fun update(game: GameEntity)

    @Delete
    suspend fun delete(game: GameEntity)
}
