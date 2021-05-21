package com.unknowncompany.genshinimpactdatabase.core.domain.model

import java.util.*

data class Character(
    var characterId: String,
    var name: String,
    var vision: String,
    var weapon: String,
    var nation: String,
    var affiliation: String,
    var constellation: String,
    var birthday: Date?,
    var rarity: Int,
    var description: String,
    var image: String,
    var isFavorite: Boolean,
)
