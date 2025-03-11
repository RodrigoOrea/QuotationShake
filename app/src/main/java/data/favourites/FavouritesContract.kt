package data.favourites

object FavouritesContract {
    const val DATABASE_NAME = "favourites_db"

    object QuotationTable {
        const val TABLE_NAME = "favourite_quotations"
        const val COLUMN_ID = "id"
        const val COLUMN_TEXT = "text"
        const val COLUMN_AUTHOR = "author"
    }
}
