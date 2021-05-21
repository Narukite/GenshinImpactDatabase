package com.unknowncompany.genshinimpactdatabase.favoritecharacter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.unknowncompany.genshinimpactdatabase.core.domain.usecase.GenshinImpactUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class FavoriteCharacterViewModel(genshinImpactUseCase: GenshinImpactUseCase) : ViewModel() {

    val favoriteCharacter = CoroutineScope(Dispatchers.Default).async {
        genshinImpactUseCase.getFavoriteCharacter().asLiveData()
    }

}