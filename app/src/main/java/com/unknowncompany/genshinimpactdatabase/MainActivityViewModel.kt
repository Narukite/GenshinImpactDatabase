package com.unknowncompany.genshinimpactdatabase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val nameQuery = MutableLiveData<String>()

}