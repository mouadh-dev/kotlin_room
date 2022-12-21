package com.example.roomwordsample.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomwordsample.NewWordActivity
import com.example.roomwordsample.R
import com.example.roomwordsample.adapter.WordListAdapter
import com.example.roomwordsample.data.entities.Word
import com.example.roomwordsample.data.repositories.WordsApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1

    private val wordViewModel: WordActivityViewModel by viewModels {
        WordActivityViewModelFactory((application  as WordsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        initView()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }


        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        wordViewModel.allWords.observe(this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { adapter.submitList(it) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                val word = Word(it)
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }


    private fun initView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val fab = findViewById<FloatingActionButton>(R.id.fab)


        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

        wordViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(it) }
        })
    }
}