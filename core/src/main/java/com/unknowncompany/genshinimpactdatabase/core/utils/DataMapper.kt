package com.unknowncompany.genshinimpactdatabase.core.utils

import android.content.res.Resources
import android.util.Log
import com.unknowncompany.genshinimpactdatabase.core.R
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity.CharacterEntity
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.response.CharacterResponse
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character
import com.unknowncompany.genshinimpactdatabase.core.ui.CharacterAdapter
import com.unknowncompany.genshinimpactdatabase.core.ui.CharacterModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object DataMapper {

    fun mapEntitiesToModels(input: List<CharacterEntity>): List<Character> =
        input.map {
            Character(
                characterId = it.characterId,
                name = it.name,
                vision = it.vision,
                weapon = it.weapon,
                nation = it.nation,
                affiliation = it.affiliation,
                constellation = it.constellation,
                birthday = it.birthday,
                rarity = it.rarity,
                description = it.description,
                image = it.image,
                isFavorite = it.isFavorite
            )
        }

    fun mapResponsesToEntities(input: List<CharacterResponse>): List<CharacterEntity> {
        val characterList = ArrayList<CharacterEntity>()

        val pattern = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())

        input.map {
            var birthday: Date? = null
            try {
                birthday = formatter.parse(it.birthday ?: "")
            } catch (e: Exception) {
                Log.d("DataMapper", "mapResponsesToEntities: ${e.message}")
            }

            val characterEntity = CharacterEntity(
                characterId = it.characterId,
                name = it.name,
                vision = it.vision,
                weapon = it.weapon,
                nation = it.nation ?: "",
                affiliation = it.affiliation ?: "",
                constellation = it.constellation,
                birthday = birthday,
                rarity = it.rarity,
                description = it.description,
                image = it.image
            )
            characterList.add(characterEntity)
        }

        return characterList
    }

    fun sortCharacterEntitiesByCharacterId(input: List<CharacterEntity>) =
        input.sortedBy { it.characterId.toInt() }

    fun setUpNameQuery(input: String): String =
        "%$input%"

    fun mapDomainModelsToPresentationModels(
        input: List<Character>,
        resources: Resources,
    ): List<CharacterModel> {
        val characterList = ArrayList<CharacterModel>()

        input.map {
            val name = if (it.name == CharacterAdapter.ITEM_NAME_EXCEPTION) resources.getString(
                R.string.traveler_name_format,
                it.name,
                it.vision)
            else it.name
            val visionWeapon = resources.getString(
                R.string.vision_weapon_format,
                it.vision,
                it.weapon)
            val nation =
                if (it.nation.isNotEmpty()) it.nation
                else resources.getString(
                    R.string.unknown_value_format,
                    resources.getString(R.string.nation))
            val affiliation =
                if (it.affiliation.isNotEmpty()) it.affiliation
                else resources.getString(
                    R.string.unknown_value_format,
                    resources.getString(R.string.affiliation))
            val birthday = "".run {
                val value: String
                if (it.birthday != null) {
                    val calendar = Calendar.getInstance()
                    calendar.time = it.birthday as Date
                    val month = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
                    val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
                    value = resources.getString(
                        R.string.birthday_format,
                        month,
                        day)
                } else {
                    value = resources.getString(
                        R.string.unknown_value_format,
                        resources.getString(R.string.birthday)
                    )
                }
                value
            }
            val rarity = rarityIntToRarityString(it.rarity)

            val characterModel = CharacterModel(
                characterId = it.characterId,
                name = name,
                visionWeapon = visionWeapon,
                nation = nation,
                affiliation = affiliation,
                constellation = it.constellation,
                birthday = birthday,
                rarity = rarity,
                description = it.description,
                image = it.image,
                isFavorite = it.isFavorite
            )
            characterList.add(characterModel)
        }

        return characterList

    }

    private fun rarityIntToRarityString(input: Int): String =
        when (input) {
            4 -> "SR"
            5 -> "SSR"
            else -> "Unknown rarity"
        }

}
