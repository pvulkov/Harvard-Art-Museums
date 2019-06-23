package com.harvard.art.museums.data.db.dao


import androidx.room.*
import com.harvard.art.museums.data.pojo.RecentSearchRecord

@Dao
interface RecentSearchesDao {


    @Transaction
    fun setRecentSearchesData(data: Collection<RecentSearchRecord>) {
        deleteAll()
        insert(data)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Collection<RecentSearchRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: RecentSearchRecord)

    @Query("DELETE FROM recent_searches_data_table")
    fun deleteAll()


    @Query("SELECT * FROM recent_searches_data_table ORDER BY timestamp DESC")
    fun fetchAll(): List<RecentSearchRecord>
}