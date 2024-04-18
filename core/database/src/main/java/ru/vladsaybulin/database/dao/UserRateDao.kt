package ru.vladsaybulin.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.userrate.PopulatedUserRateDbo
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.database.models.userrate.UserRateOrderDbo
import ru.vladsaybulin.model.userrate.UserRateStatus

@Dao
interface UserRateDao {

    @Query(
        value = """
            SELECT user_rates.* 
            FROM paged_user_rates
            INNER JOIN user_rates ON user_rate_id = user_rates.id
            WHERE status = :status AND anime_id IS NOT NULL
            ORDER BY `order`
        """
    )
    fun getPagedAnimeUserRates(status: UserRateStatus): PagingSource<Int, PopulatedUserRateDbo>

    @Query(
        value = """
            SELECT user_rates.* 
            FROM paged_user_rates
            INNER JOIN user_rates ON user_rate_id = user_rates.id
            WHERE status = :status AND manga_id IS NOT NULL
            ORDER BY `order`
        """
    )
    fun getPagedMangaUserRates(status: UserRateStatus): PagingSource<Int, PopulatedUserRateDbo>

    @Query(
        value = """
            SELECT * FROM user_rates
            WHERE status = 'watching' OR status = 'rewatching' 
            ORDER BY updated_at DESC
            LIMIT :limit
        """
    )
    fun getLastInProgressUserRates(limit: Int): Flow<List<PopulatedUserRateDbo>>

    @Query("SELECT * FROM user_rates WHERE anime_id = :animeId AND manga_id IS NULL")
    fun getAnimeUserRate(animeId: Long): Flow<UserRateEntity?>

    @Query("SELECT * FROM user_rates WHERE anime_id IS NULL AND manga_id = :mangaId")
    fun getMangaUserRate(mangaId: Long): Flow<UserRateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUserRates(userRates: List<UserRateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUserRate(userRate: UserRateEntity)

    @Insert
    suspend fun insertUserRateOrder(userRateOrder: List<UserRateOrderDbo>)

    @Query("DELETE FROM paged_user_rates")
    suspend fun deleteAllOrderedUserRates()

    @Query("DELETE FROM paged_user_rates WHERE user_rate_id IN (SELECT id FROM user_rates WHERE status = :status)")
    suspend fun deleteOrderUserRateByStatus(status: UserRateStatus)

    @Query("DELETE FROM user_rates WHERE id = :userRateId")
    suspend fun deleteUserRate(userRateId: Long)

    @Query("DELETE FROM user_rates")
    suspend fun deleteAll()

}