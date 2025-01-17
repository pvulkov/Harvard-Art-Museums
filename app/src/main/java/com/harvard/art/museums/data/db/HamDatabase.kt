package com.harvard.art.museums.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harvard.art.museums.data.db.converters.*
import com.harvard.art.museums.data.db.dao.ExhibitionRecordDao
import com.harvard.art.museums.data.db.dao.ObjectsRecordDao
import com.harvard.art.museums.data.db.dao.RecentSearchesDao
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.data.pojo.ObjectRecord
import com.harvard.art.museums.data.pojo.RecentSearchRecord


@Database(entities = [
    ExhibitionRecord::class,
    RecentSearchRecord::class,
    ObjectRecord::class
],

        version = 1)
@TypeConverters(
        RecordConverter::class,
        InfoConverter::class,
        VenueConverter::class,
        PeopleConverter::class,
        ImageConverter::class,
        PosterConverter::class,
        DetailsConverter::class,
        SeeAlsoConverter::class,
        WorkTypeConverter::class
)
abstract class HamDatabase : RoomDatabase() {

    abstract fun objectsRecordDao(): ObjectsRecordDao

    abstract fun exhibitionRecordDao(): ExhibitionRecordDao

    abstract fun recentSearchesDao(): RecentSearchesDao
}