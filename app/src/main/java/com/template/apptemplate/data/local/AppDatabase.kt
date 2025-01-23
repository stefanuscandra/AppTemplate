package com.template.apptemplate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.template.apptemplate.data.local.dao.AppDao
import com.template.apptemplate.data.local.entities.AppEntity

@Database(
    entities = [AppEntity::class],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        const val DATABASE_VERSION = 1

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app-database"
            ).build()
        }
    }
}
