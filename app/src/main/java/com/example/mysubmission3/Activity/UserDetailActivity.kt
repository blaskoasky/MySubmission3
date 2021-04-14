package com.example.mysubmission3.Activity

import android.content.ContentValues
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mysubmission3.R
import com.example.mysubmission3.adapter.SectionPageAdapter
import com.example.mysubmission3.data.Favourite
import com.example.mysubmission3.data.UserData
import com.example.mysubmission3.databinding.ActivityUserDetailBinding
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.AVATAR
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.COMPANY
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.FAVOURITE
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.LOCATION
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.NAME
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.REPOSITORY
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.example.mysubmission3.db.FavouriteHelper

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var favHelper: FavouriteHelper
    private lateinit var imgAvatar: String

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_FAVOURITE = "extra_favourite"
        const val EXTRA_POSITION = "extra_position"
    }

    private var favourites: Favourite? = null
    private var isFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favHelper = FavouriteHelper.getInstance(applicationContext)
        favHelper.open()

        favourites = intent.getParcelableExtra(EXTRA_NOTE)
        if (favourites != null) {
            val favUser = intent.getParcelableExtra<Favourite>(EXTRA_NOTE) as Favourite
            Glide.with(this)
                .load(favUser.avatar)
                .into(binding.civAvatarDt)
            binding.tvUsernameDt.text = favUser.username
            binding.tvNameDt.text = favUser.name
            binding.tvRepositoryDt.text = favUser.repository
            binding.tvCompanyDt.text = favUser.company
            binding.tvLocationDt.text = favUser.location

            imgAvatar = favUser.avatar.toString()
            val checked: Int = R.drawable.ic_baseline_favorite_24
            binding.ibtnFav.setImageResource(checked)
        } else {
            val user = intent.getParcelableExtra<UserData>(EXTRA_DATA) as UserData
            Glide.with(this)
                .load(user.avatar)
                .into(binding.civAvatarDt)
            binding.tvUsernameDt.text = user.username
            binding.tvNameDt.text = user.name
            binding.tvRepositoryDt.text = user.repository
            binding.tvCompanyDt.text = user.company
            binding.tvLocationDt.text = user.location
            imgAvatar = user.avatar.toString()
        }

        binding.ibtnFav.setOnClickListener {
            btnFav()
        }


        val sectionPageAdapter = SectionPageAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionPageAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f
    }

    private fun btnFav() {
        val unchecked: Int = R.drawable.ic_baseline_favorite_border_24
        val cheked: Int = R.drawable.ic_baseline_favorite_24

        if (isFav == true) {
            favHelper.deleteById(favourites?.username.toString())
            binding.ibtnFav.setImageResource(unchecked)
            isFav = false
        } else {
            val avatar = imgAvatar
            val username = binding.tvUsernameDt.text.toString()
            val name = binding.tvNameDt.text.toString()
            val repository = binding.tvRepositoryDt.text.toString()
            val company = binding.tvCompanyDt.text.toString()
            val location = binding.tvLocationDt.text.toString()
            val fav = "1"

            val values = ContentValues()
            values.put(AVATAR, avatar)
            values.put(USERNAME, username)
            values.put(NAME, name)
            values.put(REPOSITORY, repository)
            values.put(COMPANY, company)
            values.put(LOCATION, location)
            values.put(FAVOURITE, fav)

            isFav = true
            contentResolver.insert(CONTENT_URI, values)
            binding.ibtnFav.setImageResource(cheked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

}