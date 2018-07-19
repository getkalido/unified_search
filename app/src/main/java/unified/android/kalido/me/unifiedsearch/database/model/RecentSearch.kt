package unified.android.kalido.me.unifiedsearch.database.model

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "RecentSearch")
class RecentSearch {

    @PrimaryKey
    var id: Int? = null

    @ColumnInfo(name = "text")
    var searchText: String? = null
}

@Dao
interface RecentSearchDao {

    @get:Query("SELECT * FROM RecentSearch")
    val all: List<RecentSearch>

    @Query("SELECT COUNT(*) from RecentSearch")
    fun countUsers(): Int

    @Insert
    fun insertAll(vararg recentSearches: RecentSearch)

    @Delete
    fun delete(recentSearch: RecentSearch)
}

@Database(entities = [(RecentSearch::class)], version = 1 )
abstract class AppDatabase : RoomDatabase() {

    abstract fun recentSearchDao(): RecentSearchDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {

            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {

                        INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "user-database")
                                // allow queries on the main thread.
                                // Don't do this on a real app! See PersistenceBasicSample for an example.
                                .allowMainThreadQueries()
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}