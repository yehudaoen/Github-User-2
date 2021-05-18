package com.example.githubuser2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.githubuser2.ApiBuilder.Companion.api
import com.example.githubuser2.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.lang.Exception

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding : ActivityMainBinding
    private val list = ArrayList<DataUserGithub>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMain.setHasFixedSize(true)

        api().getUsers()?.enqueue(object:Callback<List<DataUserGithub>>{

            override fun onResponse(call: Call<List<DataUserGithub>>,
                response: Response<List<DataUserGithub>>
            ) {
                binding.rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
                val listUserAdapter = ListGithubUserAdapter(response.body()!!)
                binding.rvMain.adapter = listUserAdapter
                listUserAdapter.notifyDataSetChanged()
                binding.rvMain.visibility = View.VISIBLE

                listUserAdapter.setOnItemClickCallback(object : ListGithubUserAdapter.OnItemClickCallback{
                    override fun onItemClicked(user: DataUserGithub) {
                        showSelectedUser(user)

                        val moveToDetailIntent = Intent(this@MainActivity, GithubUserDetailActivity::class.java)
                        moveToDetailIntent.putExtra(GithubUserDetailActivity.EXTRA_USER, user)
                        startActivity(moveToDetailIntent)
                    }
                })
            }

            override fun onFailure(call: Call<List<DataUserGithub>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar, menu)
        return true

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.sv_main).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean{
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })
    }

    private fun getUserData() {
        binding.pbMa.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                binding.pbMa.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseArray = JSONArray(result)

                    for (i in 0 until responseArray.length()){
                        val jsonObject = responseArray.getJSONObject(i)
                        val name: String = jsonObject.getString("login")
                        getUserDetail(name)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                binding.pbMa.visibility = View.INVISIBLE
                val error = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUserDetail(id: String) {
        binding.pbMa.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                binding.pbMa.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val name: String? = jsonObject.getString("name").toString()
                    val avatar: String? = jsonObject.getString("avatar_url").toString()
                    val username: String? = jsonObject.getString("login").toString()
                    val repository: String? = jsonObject.getString("public_repos").toString()
                    val location: String? = jsonObject.getString("location").toString()
                    val company: String? = jsonObject.getString("company").toString()
                    list.add(
                            DataUserGithub(
                                    name,
                                    avatar,
                                    username,
                                    repository,
                                    location,
                                    company
                            )
                    )
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                binding.pbMa.visibility = View.INVISIBLE
                val error = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Bad Forbiden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            }

        })

    }



    private fun getUserSearch(id: String) {
        binding.pbMa.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                binding.pbMa.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()){
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                binding.pbMa.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbiden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showSelectedUser(user: DataUserGithub){
        Toast.makeText(this, "Kamu memilih ${user.name}", Toast.LENGTH_SHORT).show()
    }
}