package com.unknowncompany.genshinimpactdatabase.core.domain.usecase

import com.unknowncompany.genshinimpactdatabase.core.data.Resource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiResponse
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character
import com.unknowncompany.genshinimpactdatabase.core.domain.repository.IGenshinImpactRepository
import kotlinx.coroutines.flow.Flow

class GenshinImpactInteractor(private val genshinImpactRepository: IGenshinImpactRepository) :
    GenshinImpactUseCase {

    override suspend fun getCharacterNames(): Flow<ApiResponse<List<String>>> =
        genshinImpactRepository.getCharacterNames()

    override suspend fun getAllCharacter(characterNames: List<String>): Flow<Resource<List<Character>>> =
        genshinImpactRepository.getAllCharacter(characterNames)

    override suspend fun getCharacterByNameQuery(name: String): List<Character> =
        genshinImpactRepository.getCharacterByNameQuery(name)

    override suspend fun getFavoriteCharacter(): Flow<List<Character>> =
        genshinImpactRepository.getFavoriteCharacter()

    override suspend fun updateFavoriteCharacterByCharacterId(
        characterId: String,
        currentState: Boolean,
    ) {
        genshinImpactRepository.updateFavoriteCharacterByCharacterId(characterId, currentState)
    }


}