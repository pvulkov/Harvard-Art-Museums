package com.harvard.art.museums.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harvard.art.museums.data.db.converters.*
import com.harvard.art.museums.data.db.dao.ExhibitionRecordDao
import com.harvard.art.museums.data.pojo.ExhibitionRecord


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


    abstract fun exhibitionRecordDao(): ExhibitionRecordDao
}