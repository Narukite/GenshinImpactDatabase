package com.unknowncompany.genshinimpactdatabase.core.data

import com.unknowncompany.genshinimpactdatabase.core.data.source.local.LocalDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.RemoteDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiResponse
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.response.CharacterResponse
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character
import com.unknowncompany.genshinimpactdatabase.core.domain.repository.IGenshinImpactRepository
import com.unknowncompany.genshinimpactdatabase.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GenshinImpactRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : IGenshinImpactRepository {

    override suspend fun getCharacterNames(): Flow<ApiResponse<List<String>>> =
        remoteDataSource.getCharacterNames()

    override suspend fun getAllCharacter(characterNames: List<String>): Flow<Resource<List<Character>>> =
        object :
            com.unknowncompany.genshinimpactdatabase.core.data.NetworkBoundResource<List<Character>, List<CharacterResponse>>() {
            override suspend fun loadFromDB(): Flow<List<Character>> {
                return localDataSource.getAllCharacter().map {
                    DataMapper.mapEntitiesToModels(it)
                }
            }

            override fun shouldFetch(data: List<Character>?): Boolean =
                data == null || data.size != characterNames.size && characterNames.isNotEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<CharacterResponse>>> =
                remoteDataSource.getCharacters(characterNames)

            override suspend fun saveCallResult(data: List<CharacterResponse>) {
                val entities =
                    DataMapper
                        .mapResponsesToEntities(
                            data)

                val sortedEntities =
                    DataMapper
                        .sortCharacterEntitiesByCharacterId(
                            entities)

                localDataSource
                    .insertCharacter(
                        sortedEntities)
            }
        }.asFlow()

    override suspend fun getCharacterByNameQuery(name: String): List<Character> {
        return DataMapper.mapEntitiesToModels(
            localDataSource.getCharacterByNameQuery(name))
    }

    override suspend fun getFavoriteCharacter(): Flow<List<Character>> {
        return localDataSource.getFavoriteCharacter().map {
            DataMapper.mapEntitiesToModels(it)
        }
    }

    override suspend fun updateFavoriteCharacterByCharacterId(
        characterId: String,
        currentState: Boolean,
    ) {
        localDataSource
            .updateFavoriteCharacterByCharacterId(
                characterId,
                currentState)
    }

}