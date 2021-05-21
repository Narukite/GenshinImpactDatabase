package com.unknowncompany.genshinimpactdatabase.core.data.source.remote

import android.util.Log
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiConfig
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiResponse
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiService
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.response.CharacterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    fun getCharacterNames(): Flow<ApiResponse<List<String>>> {
        return flow {
            try {
                val data = apiService.getCharacterNames()
                if (data.isNullOrEmpty()) {
                    emit(ApiResponse.Empty)
                } else {
                    emit(ApiResponse.Success(data))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))

                Log.e("RemoteDataSource", "getCharacterNames: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getCharacters(characterNames: List<String>): Flow<ApiResponse<List<CharacterResponse>>> {
        return flow {
            try {
                val temporaryData = ArrayList<CharacterResponse>()

                for (i in characterNames.indices) {
                    val data = apiService.getCharacter(characterNames[i])
                    data.characterId = i.toString()
                    val image =
                        ApiConfig.BASE_URL + "characters/" + characterNames[i] + "/portrait.png"
                    data.image = image

                    temporaryData.add(data)

                    if (temporaryData.size == characterNames.size) {
                        emit(ApiResponse.Success(temporaryData))
                    }
                }

            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))

                Log.e("RemoteDataSource", "getCharacterNames: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

}