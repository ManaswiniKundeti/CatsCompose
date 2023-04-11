package com.example.catscompose.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Breed (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name")  val name: String,
    @ColumnInfo(name = "origin") val origin: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "reference_image_id") @SerializedName("reference_image_id") val referenceImageId: String?,
    @ColumnInfo(name = "life_span") @SerializedName("life_span") val lifeSpan: String?
    ) {
    companion object {
        fun mock() = Breed(
            id = "abys",
            name = "Abyssinian",
            origin = "Egypt",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            referenceImageId = "0XYvRd7oD",
            lifeSpan = "14 - 15"
        )
    }
}