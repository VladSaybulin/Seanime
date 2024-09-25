package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity

@Dao
interface OngoingAnimeDao {

    @Query(
        value = """
            SELECT animes.* 
            FROM ongoing_animes
            INNER JOIN animes ON anime_id = animes.id
            LIMIT :limit
        """
    )
    fun getOngoingAnime(limit: Int): Flow<List<AnimeEntity>>

    @Insert
    suspend fun insertAll(ongoingAnime: List<OngoingAnimeEntity>)

    @Query("DELETE FROM ongoing_animes")
    suspend fun deleteAll()


}