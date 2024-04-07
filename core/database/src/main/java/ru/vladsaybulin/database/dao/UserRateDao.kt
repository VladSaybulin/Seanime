package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.UserRateDbo

@Dao
interface UserRateDao {

    @Query("SELECT * FROM user_rates WHERE anime_id = :animeId AND manga_id IS NULL")
    fun getAnimeUserRate(animeId: Long): Flow<UserRateDbo?>

    @Query("SELECT * FROM user_rates WHERE anime_id IS NULL AND manga_id = :mangaId")
    fun getMangaUserRate(mangaId: Long): Flow<UserRateDbo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUserRates(userRates: List<UserRateDbo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUserRate(userRate: UserRateDbo)

    @Query("DELETE FROM user_rates WHERE id = :userRateId")
    suspend fun deleteUserRate(userRateId: Long)

    @Query("DELETE FROM user_rates")
    suspend fun deleteAll()

}