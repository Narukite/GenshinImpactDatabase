package com.unknowncompany.genshinimpactdatabase.core.data.source.local

import com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity.CharacterEntity
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.room.GenshinImpactDao
import com.unknowncompany.genshinimpactdatabase.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val genshinImpactDao: GenshinImpactDao) {

    fun getAllCharacter(): Flow<List<CharacterEntity>> =
        genshinImpactDao.getAllCharacter()

    suspend fun getCharacterByNameQuery(name: String): List<CharacterEntity> =
        genshinImpactDao.getCharacterByNameQuery(
            DataMapper.setUpNameQuery(name))

    fun getFavoriteCharacter(): Flow<List<CharacterEntity>> =
        genshinImpactDao.getFavoriteCharacter()

    suspend fun insertCharacter(data: List<CharacterEntity>) =
        genshinImpactDao.insertCharacter(data)

    fun updateFavoriteCharacterByCharacterId(characterId: String, currentState: Boolean) {
        val isFavorite = !currentState
        genshinImpactDao.updateFavoriteCharacterByCharacterId(characterId, isFavorite)
    }

}