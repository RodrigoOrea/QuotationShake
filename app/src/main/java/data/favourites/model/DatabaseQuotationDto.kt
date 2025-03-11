package com.tuapp.data.favourites.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import data.favourites.FavouritesContract.QuotationTable

@Entity(tableName = QuotationTable.TABLE_NAME)
data class DatabaseQuotationDto(
    @PrimaryKey val id: String,
    @ColumnInfo(name = QuotationTable.COLUMN_TEXT) val text: String,
    @ColumnInfo(name = QuotationTable.COLUMN_AUTHOR) val author: String
)
