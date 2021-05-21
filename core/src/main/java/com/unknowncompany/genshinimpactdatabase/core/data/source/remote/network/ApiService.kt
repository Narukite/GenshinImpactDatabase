package com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network

import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.response.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("characters")
    suspend fun getCharacterNames(): List<String>?

    @GET("characters/{name}")
    suspend fun getCharacter(
        @Path("name")
        name: String,
    ): CharacterResponse

}