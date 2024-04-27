package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vladsaybulin.network.models.character.NetworkCharacterDetails
import javax.inject.Inject

interface CharacterApi {

    @GET("api/characters/{id}")
    suspend fun getCharacterDetails(@Path("id") characterId: Long): NetworkCharacterDetails?

}

class CharacterDataSource @Inject constructor(retrofit: Retrofit) {

    private val api = retrofit.create<CharacterApi>()

    suspend fun getCharacterDetails(characterId: Long): NetworkCharacterDetails =
        checkNotNull(api.getCharacterDetails(characterId)) {
            "Character not found. Character ID = $characterId"
        }
}