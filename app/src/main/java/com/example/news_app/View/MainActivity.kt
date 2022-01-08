package com.example.news_app.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news_app.Adapter.News_Adapter
import com.example.news_app.Repository.NewsRepository
import com.example.news_app.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.news_app.ViewModel.NewsViewModel
import com.example.news_app.ViewModel.NewsViewModelFactory
import com.example.news_app.databinding.ActivityMainBinding
import com.example.news_app.util.Constants.Companion.Query_Page_Size
import com.example.news_app.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding:ActivityMainBinding
    val TAG = "BreakingNews"
    val TAG1="SearchingNews"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository= NewsRepository()
        newsViewModel=ViewModelProvider(this, NewsViewModelFactory(repository)).get(NewsViewModel::class.java)

        val adapter= News_Adapter()
        binding.rvnews.adapter=adapter
        binding.rvnews.addOnScrollListener(srcollListener)

        var job:Job?=null
        binding.searchview.addTextChangedListener {editale->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editale?.let {
                    if(editale.toString().isNotEmpty())
                    {
                        newsViewModel.searchNews(editale.toString())
                    }
                }
            }
        }
        //For search
        newsViewModel.searchNews.observe(this, Observer { response ->
            when(response)
            {
                is Resource.Success ->
                {
                    hideProgressbar()
                    response.data?.let { newsData ->
                        adapter.differ.submitList(newsData.articles.toList())
                        val totalPages=newsData.totalResults/ Query_Page_Size+2
                        isLastPage=newsViewModel.searchNewsPage==totalPages
                        if(isLastPage)
                        {
                            binding.rvnews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error ->
                {
                    hideProgressbar()
                    response.message?.let {message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading ->
                {
                    showProgressBar()
                }

            }
        })

        newsViewModel.breakingNews.observe(this, Observer { response ->
            when(response)
            {
                is Resource.Success ->
                {
                    hideProgressbar()
                    response.data?.let { newsData ->
                        adapter.differ.submitList(newsData.articles.toList())
                        val totalPages=newsData.totalResults/ Query_Page_Size+2
                        isLastPage=newsViewModel.breakingNewsPage==totalPages
                        if(isLastPage)
                        {
                            binding.rvnews.setPadding(0,0,0,0)
                        }

                    }
                }
                is Resource.Error ->
                {
                   hideProgressbar()
                    response.message?.let {message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading ->
                {
                    showProgressBar()
                }

            }
        })

    }

    private fun hideProgressbar() {
       binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading=true
    }
    var isLoading=false
    var isLastPage=false
    var isScrolling=false
    var srcollListener=object :RecyclerView.OnScrollListener()
    {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalitemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPage=!isLoading && !isLastPage
            val isAtLastItem=firstVisibleItemPosition+visibleItemCount >= totalitemCount
            val isNotAtBeginning=firstVisibleItemPosition >=0
            val isTotalMoreThanVisible=totalitemCount >= Query_Page_Size
            val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible
                    && isScrolling
            if(shouldPaginate)
            {
                newsViewModel.getBreakingNews("us")
                newsViewModel.searchNews(binding.searchview.text.toString())
                isScrolling=false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling=true
            }
        }
    }

}
