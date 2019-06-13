package com.harvard.art.museums.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harvard.art.museums.data.db.converters.*
import com.harvard.art.museums.data.db.dao.ExhibitionRecordDao
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.data.pojo.Exhibitions


@Database(entities = [
    ExhibitionRecord::class
],

        version = 1)
@TypeConverters(
        RecordConverter::class,
        InfoConverter::class,
        VenueConverter::class,
        PeopleConverter::class,
        ImageConverter::class,
        PosterConverter::class
)
abstract class HamDatabase : RoomDatabase() {


//    companion object {
//        private var INSTANCE: HamDatabase? = null
//
//        @JvmStatic
//        fun getDatabase(context: Context): HamDatabase {
//            if (INSTANCE == null) {
//                synchronized(HamDatabase::class) {
//                    if (INSTANCE == null) {
//                        INSTANCE = Room.databaseBuilder(
//                                context.applicationContext,
//                                HamDatabase::class.java, "harvard_art_museums_database.db"
//                        )
//                                .fallbackToDestructiveMigration()
//                                .build()
//                    }
//                }
//            }
//            return INSTANCE!!
//        }
//    }


    //------------------------------------------------------------
    // DAOs
    //------------------------------------------------------------


    abstract fun exhibitionRecordDao(): ExhibitionRecordDao

}