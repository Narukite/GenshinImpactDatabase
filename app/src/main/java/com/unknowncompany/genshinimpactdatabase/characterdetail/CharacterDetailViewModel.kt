package com.unknowncompany.genshinimpactdatabase.characterdetail

import androidx.lifecycle.ViewModel
import com.unknowncompany.genshinimpactdatabase.core.domain.usecase.GenshinImpactUseCase

class CharacterDetailViewModel
    (private val genshinImpactUseCase: GenshinImpactUseCase) :
    ViewModel() {

    suspend fun updateFavoriteCharacterByCharacterId(charactedId: String, currentState: Boolean) =
        genshinImpactUseCase.updateFavoriteCharacterByCharacterId(charactedId, currentState)

}