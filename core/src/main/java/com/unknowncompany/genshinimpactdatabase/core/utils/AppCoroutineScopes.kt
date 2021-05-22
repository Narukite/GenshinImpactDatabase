package com.unknowncompany.genshinimpactdatabase.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AppCoroutineScopes(
    private val io: CoroutineScope,
    private val default: CoroutineScope,
) {

    constructor() : this(
        CoroutineScope(Dispatchers.IO),
        CoroutineScope(Dispatchers.Default)
    )

    fun io(): CoroutineScope = io
    fun default(): CoroutineScope = default

}
