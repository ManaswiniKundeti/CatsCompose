package com.example.catscompose.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catscompose.model.Breed

@Dao
interface BreedDao {

    @Query("SELECT * FROM Breed")
    suspend fun getBreedList(): List<Breed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreedList(breed: List<Breed>)
}