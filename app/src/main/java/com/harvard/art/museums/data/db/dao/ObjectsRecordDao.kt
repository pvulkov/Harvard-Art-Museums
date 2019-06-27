package com.harvard.art.museums.data.db.dao


import androidx.room.*
import com.harvard.art.museums.data.pojo.ObjectRecord
import io.reactivex.Single

@Dao
interface ObjectsRecordDao {


    @Transaction
    fun setObjectRecordsData(data: Collection<ObjectRecord>) {
        deleteAll()
        insert(data)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Collection<ObjectRecord>)


    @Query("DELETE FROM object_records_data_table")
    fun deleteAll()


    @Query("SELECT * FROM object_records_data_table")
    fun fetchAll(): Single<List<ObjectRecord>>

    @Query("SELECT * FROM object_records_data_table WHERE cur = :next")
    fun fetchAll(next: String): Single<List<ObjectRecord>>

//    @Query("SELECT * FROM exhibition_records_data_table WHERE exhibitionid = :id ")
//    fun getById(id: Int): Single<ObjectRecord>
}