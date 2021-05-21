package com.unknowncompany.genshinimpactdatabase.core.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterModel(
    var characterId: String,
    var name: String,
    var visionWeapon: String,
    var nation: String,
    var affiliation: String,
    var constellation: String,
    var birthday: String,
    var rarity: String,
    var description: String,
    var image: String,
    var isFavorite: Boolean,
) : Parcelable
