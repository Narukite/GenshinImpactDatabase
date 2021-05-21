package com.unknowncompany.genshinimpactdatabase.core.domain.usecase

import com.unknowncompany.genshinimpactdatabase.core.data.Resource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiResponse
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface GenshinImpactUseCase {

    fun getCharacterNames(): Flow<ApiResponse<List<String>>>

    fun getAllCharacter(characterNames: List<String>): Flow<Resource<List<Character>>>

    suspend fun getCharacterByNameQuery(name: String): List<Character>

    suspend fun getFavoriteCharacter(): Flow<List<Character>>

    fun updateFavoriteCharacterByCharacterId(characterId: String, currentState: Boolean)
}