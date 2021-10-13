package com.challenge.felipeajc.pixabaysearch.data.local

import android.content.Context
import androidx.room.*
import com.challenge.felipeajc.pixabaysearch.data.entities.PixabayImage

@Database(entities = [PixabayImage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pixabayImagesDao(): PixabayImageDao
}

@Dao
interface PixabayImageDao {
    @Query("SELECT * FROM tb_images")
    suspend fun getAllImages(): List<PixabayImage>

    @Insert
    suspend fun insertAllImages(images: List<PixabayImage>)

    @Query("DELETE FROM tb_images")
    suspend fun deleteAllImages()
}

fun createPixabayImageDB(context: Context) = Room.databaseBuilder(
    context,
    AppDatabase::class.java, "pixabaysearch_db"
).build()