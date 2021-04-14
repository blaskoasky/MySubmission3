package com.example.mysubmission3.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.Activity.UserDetailActivity
import com.example.mysubmission3.data.UserData
import com.example.mysubmission3.adapter.FollowingAdapter
import com.example.mysubmission3.data.Favourite
import com.example.mysubmission3.databinding.FragmentFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingFragment : Fragment() {

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
    }

    private var list: ArrayList<UserData> = ArrayList()

    private var favourites: Favourite? = null
    private lateinit var dataFavourite: Favourite
    private lateinit var userData: UserData


    private lateinit var adapter: FollowingAdapter
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding
    private var token = "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"



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowingAdapter(list)
        list.clear()


        favourites = activity?.intent?.getParcelableExtra(UserDetailActivity.EXTRA_NOTE)

        if (favourites != null) {
            dataFavourite = activity?.intent?.getParcelableExtra<Favourite>(UserDetailActivity.EXTRA_NOTE) as Favourite
            getFollowing(dataFavourite.username.toString())
        } else {
            userData = activity?.intent?.getParcelableExtra<UserData>(UserDetailActivity.EXTRA_DATA) as UserData
            getFollowing(userData.username.toString())
        }
    }

    private fun showRecyclerList() {
        binding?.rvFollowing?.layoutManager = LinearLayoutManager(activity)
        binding?.rvFollowing?.adapter = adapter
    }

    fun getFollowing(id: String) {
        binding?.pbFollowing?.visibility = View.VISIBLE

        val aClient = AsyncHttpClient()
        val url = "https://api.github.com/users/$id/following"
        aClient.addHeader("Authorization", "token ${token}")
        aClient.addHeader("User-Agent", "request")
        aClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding?.pbFollowing?.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String? = jsonObject.getString("login")
                        setFollowing(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding?.pbFollowing?.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun setFollowing(id: String?) {
        binding?.pbFollowing?.visibility = View.VISIBLE

        val aClient = AsyncHttpClient()
        val url = "https://api.github.com/users/$id"
        aClient.addHeader("Authorization", "token ${token}")
        aClient.addHeader("User-Agent", "request")
        aClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding?.pbFollowing?.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val user = UserData()
                    user.username = jsonObject.getString("login")
                    user.name = jsonObject.getString("name")
                    user.avatar = jsonObject.getString("avatar_url").toString()
                    user.company = jsonObject.getString("company").toString()
                    user.location = jsonObject.getString("location").toString()
                    user.repository = jsonObject.getString("public_repos")
                    user.followers = jsonObject.getString("followers")
                    user.following = jsonObject.getString("following")
                    list.add(user)
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding?.pbFollowing?.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

}