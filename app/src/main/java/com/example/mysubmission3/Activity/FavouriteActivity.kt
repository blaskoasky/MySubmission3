package com.example.mysubmission3.Activity

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.adapter.FavouriteAdapter
import com.example.mysubmission3.databinding.ActivityFavouriteBinding
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.mysubmission3.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var adapter: FavouriteAdapter

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFavourite.layoutManager = LinearLayoutManager(this)
        binding.rvFavourite.setHasFixedSize(true)

        adapter = FavouriteAdapter(this)
        binding.rvFavourite.adapter = adapter

        val handlerThread = HandlerThread("Data Observer")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavsAsync()
            }
        }
    }

    private fun loadFavsAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            binding.pbFavourite.visibility = View.VISIBLE

            val defferedFavs = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favourites = defferedFavs.await()
            binding.pbFavourite.visibility = View.INVISIBLE
            if (favourites.size > 0) {
                adapter.listFavourite = favourites
            } else {
                adapter.listFavourite = ArrayList()
                showSnackBarMessage("No Data")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavourite)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvFavourite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadFavsAsync()
    }

}