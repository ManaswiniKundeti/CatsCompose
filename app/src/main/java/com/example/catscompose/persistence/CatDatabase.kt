package com.example.catscompose.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catscompose.model.Breed

@Database(entities = [Breed::class], version = 3, exportSchema = true)
abstract class CatDatabase: RoomDatabase() {

    abstract fun breedDao(): BreedDao
}