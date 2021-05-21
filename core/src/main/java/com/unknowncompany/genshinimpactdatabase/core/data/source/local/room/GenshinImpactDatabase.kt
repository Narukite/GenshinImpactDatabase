package com.unknowncompany.genshinimpactdatabase.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class GenshinImpactDatabase : RoomDatabase() {

    abstract fun genshinImpactDao(): GenshinImpactDao

}