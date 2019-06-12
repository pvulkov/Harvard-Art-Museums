package com.harvard.art.museums.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harvard.art.museums.data.db.converters.InfoConverter
import com.harvard.art.museums.data.db.converters.RecordConverter
import com.harvard.art.museums.data.db.dao.ExhibitionsDao
import com.harvard.art.museums.data.pojo.Exhibitions


@Database(entities = [Exhibitions::class], version = 1)
@TypeConverters(
        RecordConverter::class,
        InfoConverter::class
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

    abstract fun exhibitionsDao(): ExhibitionsDao

}