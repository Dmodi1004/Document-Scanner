package com.example.documentscanner.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.documentscanner.data.local.converters.DateTypeConverter
import com.example.documentscanner.data.local.dao.PdfDao
import com.example.documentscanner.models.PdfEntity

@Database(
    entities = [PdfEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateTypeConverter::class)
abstract class PdfDataBase : RoomDatabase() {

    abstract val pdfDao: PdfDao

    companion object {
        @Volatile
        private var INSTANCE: PdfDataBase? = null

        fun getInstance(context: Context): PdfDataBase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PdfDataBase::class.java,
                    "pdf_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }

    }

}