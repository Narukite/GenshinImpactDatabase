package com.unknowncompany.genshinimpactdatabase.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CharacterResponse(

    var characterId: String,

    var image: String,

    @field:SerializedName("birthday")
    val birthday: String?,

    @field:SerializedName("skillTalents")
    val skillTalents: List<SkillTalentsItem>,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("nation")
    val nation: String?,

    @field:SerializedName("constellations")
    val constellations: List<ConstellationsItem>,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("weapon_type")
    val weaponType: String,

    @field:SerializedName("specialDish")
    val specialDish: String,

    @field:SerializedName("passiveTalents")
    val passiveTalents: List<PassiveTalentsItem>,

    @field:SerializedName("title")
    var title: String?,

    @field:SerializedName("vision_key")
    val visionKey: String,

    @field:SerializedName("vision")
    val vision: String,

    @field:SerializedName("weapon")
    val weapon: String,

    @field:SerializedName("constellation")
    val constellation: String,

    @field:SerializedName("affiliation")
    val affiliation: String?,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("rarity")
    val rarity: Int,
)

data class UpgradesItem(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("value")
    val value: String,
)

data class SkillTalentsItem(

    @field:SerializedName("unlock")
    val unlock: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("upgrades")
    val upgrades: List<UpgradesItem>,
)

data class ConstellationsItem(

    @field:SerializedName("unlock")
    val unlock: String,

    @field:SerializedName("level")
    val level: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,
)

data class PassiveTalentsItem(

    @field:SerializedName("unlock")
    val unlock: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("level")
    val level: Int,
)
