package com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "character")
data class CharacterEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "characterId")
    var characterId: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "vision")
    var vision: String,

    @ColumnInfo(name = "weapon")
    var weapon: String,

    @ColumnInfo(name = "nation")
    var nation: String,

    @ColumnInfo(name = "affiliation")
    var affiliation: String,

    @ColumnInfo(name = "constellation")
    var constellation: String,

    @ColumnInfo(name = "birthday")
    var birthday: Date?,

    @ColumnInfo(name = "rarity")
    var rarity: Int,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,
)
