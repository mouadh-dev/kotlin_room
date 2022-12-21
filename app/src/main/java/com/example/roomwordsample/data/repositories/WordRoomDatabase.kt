package com.example.roomwordsample.data.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomwordsample.data.entities.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordRoomDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANSE?.let { database ->
                scope.launch (Dispatchers.IO){
                    populateDatabase(database.wordDao())


                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            //Delete all content here
            wordDao.deleteAll()

            //Add sample words
            var word = Word("Test Word")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)
            word = Word("hello it's me")
            wordDao.insert(word)
        }
    }

    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANSE: WordRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope):WordRoomDatabase {
            return INSTANSE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANSE = instance
                instance
            }
        }
    }
}