package com.unknowncompany.genshinimpactdatabase.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenshinImpactDao {

    @Query("SELECT * FROM character")
    fun getAllCharacter(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM character WHERE name LIKE :name")
    suspend fun getCharacterByNameQuery(name: String): List<CharacterEntity>

    @Query("SELECT * FROM character where isFavorite = 1")
    fun getFavoriteCharacter(): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(data: List<CharacterEntity>)

    @Query("UPDATE character SET isFavorite = :isFavorite WHERE characterId = :characterId")
    fun updateFavoriteCharacterByCharacterId(characterId: String, isFavorite: Boolean)

}