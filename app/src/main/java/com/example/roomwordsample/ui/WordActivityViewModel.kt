package com.example.roomwordsample.ui

import androidx.lifecycle.*
import com.example.roomwordsample.data.entities.Word
import com.example.roomwordsample.data.repositories.WordRepository
import kotlinx.coroutines.launch

class WordActivityViewModel(private val repository: WordRepository): ViewModel() {

    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}
class WordActivityViewModelFactory(private val repository: WordRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordActivityViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return WordActivityViewModel(repository) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}