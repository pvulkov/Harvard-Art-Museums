package com.harvard.art.museums.data.db.dao


import androidx.room.*
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExhibitionRecordDao {


    @Transaction
    fun setExhibitionRecordsData(data: Collection<ExhibitionRecord>) {
        deleteAll()
        insert(data)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Collection<ExhibitionRecord>)


    @Query("DELETE FROM exhibition_records_data_table")
    fun deleteAll()


    @Query("SELECT * FROM exhibition_records_data_table")
    fun fetchAll(): Single<List<ExhibitionRecord>>

    @Query("SELECT * FROM  exhibition_records_data_table WHERE cur = :next")
    fun fetchAll(next: String): Single<List<ExhibitionRecord>>
}