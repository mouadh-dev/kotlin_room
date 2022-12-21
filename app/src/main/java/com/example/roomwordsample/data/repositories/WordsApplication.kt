package com.example.roomwordsample.data.repositories

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication: Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())


    // Create database instance
    val database by lazy { WordRoomDatabase.getDatabase(this,applicationScope) }

    //Create Repository instance
    val repository by lazy { WordRepository(database.wordDao()) }
}