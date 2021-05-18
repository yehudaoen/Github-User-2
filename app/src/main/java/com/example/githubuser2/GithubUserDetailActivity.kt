package com.example.githubuser2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GithubUserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val  TAB_TITLES = intArrayOf(
                R.string.tab_text_1,
                R.string.tab_text_2
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_detail)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)

        val layout: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =  layout.inflate(R.layout.activity_github_user_detail, null)

        val tabs = view.findViewById<TabLayout>(R.id.tl_detail)

        val viewPager = view.findViewById<ViewPager2>(R.id.vp_detail)
        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, viewPager)
        { tab, position -> tab.text = resources.getString(TAB_TITLES[position]) }
                .attach()
        supportActionBar?.elevation = 0f


        val tvObject:TextView = findViewById(R.id.tv_name_detail)
        val user = intent.getParcelableExtra<DataUserGithub>(EXTRA_USER) as DataUserGithub
        val name = " ${user.name.toString()}"
        tvObject.text = name

        val usernameDetail = findViewById<TextView>(R.id.tv_username_detail)
        usernameDetail.text = user.username

        val repositoryDetail = findViewById<TextView>(R.id.tv_repository_detail_number)
        repositoryDetail.text = user.repository

        val imgView:ImageView = findViewById(R.id.ci_detail)
        Glide.with(this)
                .load(user.avatar)
                .into(imgView)

        val locationDetail = findViewById<TextView>(R.id.tv_location_detail)
        locationDetail.text = user.location

        val companyDetail = findViewById<TextView>(R.id.tv_company_detail)
        companyDetail.text = user.company
    }
}