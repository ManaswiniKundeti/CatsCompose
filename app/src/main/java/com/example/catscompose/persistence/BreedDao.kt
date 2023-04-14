package com.example.catscompose.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catscompose.model.Breed

@Dao
interface BreedDao {

    @Query("SELECT * FROM Breed")
    suspend fun getBreeds(): List<Breed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreeds(breed: List<Breed>)

    @Query("SELECT * FROM Breed WHERE id = :id_")
    suspend fun getBreedById(id_: String): Breed?
}