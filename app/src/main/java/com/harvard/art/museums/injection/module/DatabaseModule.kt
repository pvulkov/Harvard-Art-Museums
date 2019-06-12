package com.harvard.art.museums.injection.module

import android.content.Context
import androidx.room.Room
import com.harvard.art.museums.data.db.HamDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Module providing database dependencies
 */
@Module(includes = [(ContextModule::class)])
object DatabaseModule {


    /**
     * Provides database implementation.
     * @param context context needed to instantiate the DB object
     * @return HamDatabase instance.
     */
    @Provides
    @Singleton
    @JvmStatic
    internal fun provideDatabase(context: Context): HamDatabase {
        return Room.databaseBuilder(context, HamDatabase::class.java, "DB_NAME")
                .fallbackToDestructiveMigration()
                .build()
    }


}