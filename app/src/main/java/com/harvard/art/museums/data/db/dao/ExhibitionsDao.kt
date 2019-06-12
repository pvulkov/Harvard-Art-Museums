package com.harvard.art.museums.data.db.dao


import androidx.room.*
import com.harvard.art.museums.data.pojo.Exhibitions
import io.reactivex.Single

@Dao
interface ExhibitionsDao {


    @Transaction
    fun setExhibitionsData(data: Collection<Exhibitions>) {
        deleteAll()
        insert(data)
    }

    @Insert
    fun insert(data: Exhibitions)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Collection<Exhibitions>)


    @Query("DELETE FROM exhibitions_data_table")
    fun deleteAll()


    @Query("SELECT * FROM exhibitions_data_table")
    fun fetchAll(): Single<List<Exhibitions>>


//    @Query("SELECT * FROM site_roster_data_table WHERE rosterId = :rosterId ")
//    fun getRosterById(rosterId: Long): Single<SiteRosterData>
//
//
//    @Query("SELECT rosterId FROM site_roster_data_table WHERE firstName = :firstName AND lastName = :lastName ")
//    fun getRosterID(firstName: String, lastName: String): Single<Long>
//
//
//    @Query("SELECT path FROM site_roster_data_table WHERE firstName = :firstName AND lastName = :lastName AND rosterId = :rosterId")
//    fun getRosterPath(firstName: String, lastName: String, rosterId: Long): Single<String>

}