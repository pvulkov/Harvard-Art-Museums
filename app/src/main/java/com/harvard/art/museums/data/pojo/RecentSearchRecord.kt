package com.harvard.art.museums.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harvard.art.museums.features.search.SearchResultViewType


@Entity(tableName = "recent_searches_data_table")
data class RecentSearchRecord(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val viewType: Int,
        val text: String,
        val objectId: Int? = null,
        val imageUrl: String? = null,
        var timestamp: Long = System.currentTimeMillis()
)
