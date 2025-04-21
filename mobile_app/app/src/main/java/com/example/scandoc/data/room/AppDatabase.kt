package com.example.scandoc.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scandoc.data.room.dao.DocumentSetDao
import com.example.scandoc.data.room.models.RoomDocumentSet
import javax.inject.Singleton

private const val DATABASE_NAME = "ScanDocAppDB"

@Database(entities = [RoomDocumentSet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentSetDao(): DocumentSetDao

    internal companion object {
        @Singleton
        fun getInstance(context: Context): AppDatabase =
            Room
                .databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_NAME,
                ).addMigrations()
                .build()
    }
}
