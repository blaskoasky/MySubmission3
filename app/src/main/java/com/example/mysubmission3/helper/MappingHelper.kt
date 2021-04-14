package com.example.mysubmission3.helper

import android.database.Cursor
import com.example.mysubmission3.data.Favourite
import com.example.mysubmission3.db.DatabaseContract
import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(favsCursor: Cursor?): ArrayList<Favourite>{
        val favsList = ArrayList<Favourite>()
        favsCursor?.apply {
            while (moveToNext()) {
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.NAME))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.REPOSITORY))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.LOCATION))
                val favourite = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FAVOURITE))

                favsList.add(Favourite(avatar, username, name, repository, company, location, favourite))
            }
        }
        return favsList
    }
}