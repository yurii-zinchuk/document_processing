package com.zinchuk.scandoc.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zinchuk.scandoc.data.room.models.RoomDocumentSet

@Dao
interface DocumentSetDao {
    @Query("SELECT * FROM document_set ORDER BY timestamp DESC")
    fun getAllDocumentSets(): PagingSource<Int, RoomDocumentSet>

    @Query("SELECT * FROM document_set WHERE uuid = :uuid")
    suspend fun getDocumentSetByUUID(uuid: String): RoomDocumentSet

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDocumentSet(documentSet: RoomDocumentSet)

    @Query("DELETE FROM document_set WHERE uuid = :uuid")
    suspend fun deleteDocumentSet(uuid: String)

    @Update
    suspend fun updateDocumentSet(documentSet: RoomDocumentSet)
}
