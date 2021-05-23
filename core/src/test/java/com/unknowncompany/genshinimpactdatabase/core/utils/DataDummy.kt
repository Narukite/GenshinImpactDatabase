package com.unknowncompany.genshinimpactdatabase.core.utils

import com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity.CharacterEntity
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character

object DataDummy {

    fun getListCharacterEntityDummy() =
        listOf(getCharacterEntityDummy())

    fun getCharacterEntityDummy() =
        CharacterEntity("dummy", "", "", "", "", "", "", null, 0, "", "", false)

    fun getListCharacterDummy() =
        listOf(getCharacterDummy())

    fun getCharacterDummy() =
        Character("dummy", "", "", "", "", "", "", null, 0, "", "", false)

}