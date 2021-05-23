package com.unknowncompany.genshinimpactdatabase.core.utils

import com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity.CharacterEntity

object DataDummy {

    fun getListCharacterEntityDummy() =
        listOf(getCharacterEntityDummy())

    private fun getCharacterEntityDummy() =
        CharacterEntity("dummy", "", "", "", "", "", "", null, 0, "", "", false)

}