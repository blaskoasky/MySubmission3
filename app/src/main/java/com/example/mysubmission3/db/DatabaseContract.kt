package com.example.mysubmission3.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.example.mysubmission3"
    const val SCHEME = "content"

    class FavColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favourite_user"
            const val AVATAR = "avatar"
            const val USERNAME = "username"
            const val NAME = "name"
            const val REPOSITORY = "repository"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val FAVOURITE = "isFav"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}