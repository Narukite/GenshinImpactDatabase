package com.unknowncompany.genshinimpactdatabase.core.data

import com.unknowncompany.genshinimpactdatabase.core.data.source.local.LocalDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.RemoteDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiResponse
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.response.CharacterResponse
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character
import com.unknowncompany.genshinimpactdatabase.core.domain.repository.IGenshinImpactRepository
import com.unknowncompany.genshinimpactdatabase.core.utils.AppExecutors
import com.unknowncompany.genshinimpactdatabase.core.utils.DataMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GenshinImpactRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
) : IGenshinImpactRepository {

    override fun getCharacterNames(): Flow<ApiResponse<List<String>>> =
        remoteDataSource.getCharacterNames()

    override fun getAllCharacter(characterNames: List<String>): Flow<Resource<List<Character>>> =
        object :
            com.unknowncompany.genshinimpactdatabase.core.data.NetworkBoundResource<List<Character>, List<CharacterResponse>>() {
            override suspend fun loadFromDB(): Flow<List<Character>> {
                val data = CoroutineScope(Dispatchers.Default).async {
                    localDataSource.getAllCharacter().map {
                        DataMapper.mapEntitiesToModels(it)
                    }
                }

                return data.await()
            }

            override fun shouldFetch(data: List<Character>?): Boolean =
                data == null || data.size != characterNames.size && characterNames.isNotEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<CharacterResponse>>> =
                remoteDataSource.getCharacters(characterNames)

            override suspend fun saveCallResult(data: List<CharacterResponse>) {
                val entities = CoroutineScope(Dispatchers.Default).async {
                    DataMapper
                        .mapResponsesToEntities(
                            data)
                }

                val sortedEntities = CoroutineScope(Dispatchers.Default).async {
                    DataMapper
                        .sortCharacterEntitiesByCharacterId(
                            entities.await())
                }

                localDataSource
                    .insertCharacter(
                        sortedEntities.await())
            }

        }.asFlow()

    override suspend fun getCharacterByNameQuery(name: String): List<Character> {
        val data = CoroutineScope(Dispatchers.Default).async {
            DataMapper.mapEntitiesToModels(
                localDataSource.getCharacterByNameQuery(name))
        }

        return data.await()
    }

    override suspend fun getFavoriteCharacter(): Flow<List<Character>> {
        val data = CoroutineScope(Dispatchers.Default).async {
            localDataSource.getFavoriteCharacter().map {
                DataMapper.mapEntitiesToModels(it)
            }
        }

        return data.await()
    }

    override fun updateFavoriteCharacterByCharacterId(characterId: String, currentState: Boolean) {
        appExecutors
            .diskIO()
            .execute {
                localDataSource
                    .updateFavoriteCharacterByCharacterId(
                        characterId,
                        currentState)
            }
    }

}