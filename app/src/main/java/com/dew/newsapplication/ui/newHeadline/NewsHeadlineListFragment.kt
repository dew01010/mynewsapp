package com.dew.newsapplication.ui.newHeadline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dew.newsapplication.R
import com.dew.newsapplication.common.BaseFrag
import com.dew.newsapplication.common.ItemOffsetDecoration
import com.dew.newsapplication.common.OnRetryConnectionListener
import com.dew.newsapplication.databinding.FragmentNewsHeadlineListBinding
import com.dew.newsapplication.model.ArticleInfo
import com.dew.newsapplication.model.NewsSource
import com.dew.newsapplication.ui.newHeadline.adapter.NewsHeadlineAdapter
import com.dew.newsapplication.ui.webView.WebViewActivity
import com.dew.newsapplication.viewModel.NewsViewModel
import com.msewa.healthism.util.Status

/**
 * A NewsHeadlineListFragment  containing a  news list form different news sources.
 */
class NewsHeadlineListFragment : BaseFrag(), NewsHeadlineAdapter.NewsHeadlineAdapterCallback,
    OnRetryConnectionListener {

    // viewModel
    private val viewModel: NewsViewModel by viewModels()

    //binding
    private  var _binding:FragmentNewsHeadlineListBinding?=null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var newsSource: NewsSource? = null
    private lateinit var myAdapter: NewsHeadlineAdapter
    private lateinit var list: ArrayList<ArticleInfo?>
    private var isScrolling: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsSource = arguments?.getParcelable(SOURCE)
        list = arrayListOf()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentNewsHeadlineListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initial status of views
        binding.loadingLayout.visibility = View.GONE
        binding.noDataTv.visibility=View.GONE
        setUpAdapter()
        setScrollListener()
        getSourceNews()
    }

    private fun setUpAdapter() {
        myAdapter = NewsHeadlineAdapter(requireContext(),list,this@NewsHeadlineListFragment)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            ItemOffsetDecoration(
                context?.resources!!.getDimensionPixelOffset(
                    R.dimen.space
                ), 1
            )
        )
        binding.recyclerView.adapter = myAdapter
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val SOURCE = "news_source_detail"

        /**
         * Returns a new instance of this fragment for the given list
         * number.
         */
        @JvmStatic
        fun newInstance(newsSource: NewsSource): NewsHeadlineListFragment {
            return NewsHeadlineListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SOURCE, newsSource)
                }
            }
        }
    }

    // this method is
    private fun getSourceNews() {
        viewModel.fetchNewsHeadlines(newsSource?.id!!)
        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            when(it.code){
                Status.LOADING->{
                   showLoading()
                }
                Status.SUCCESS->{
                    dismissLoading()
                    if(!it.data?.articles.isNullOrEmpty()){
                        this.list.addAll(it.data?.articles!!)
                        myAdapter.notifyDataSetChanged()
                    }
                    if(list.size>0) binding.noDataTv.visibility=View.GONE else  binding.noDataTv.visibility=View.VISIBLE
                }
                Status.ERROR->{
                    showMsg(it.message!!)
                }
                Status.NO_INTERNET->{
                    noInternet(this@NewsHeadlineListFragment,0)
                }
            }
        })
    }


    // this is callback function form NewsHeadlineAdapter it handles click action on Item
    override fun onClickRow(url: String) {
        startActivity(WebViewActivity.newIntent(requireActivity(),url))
    }

    override fun onDestroy() {
        super.onDestroy()
        // clear binding
        _binding = null
    }

    // this method called, if internet connection lost
    override fun onConnectionRetry(value: Int) {
        viewModel.fetchNewsHeadlines(newsSource?.id!!)
    }

    // this method handle list scrolling to check if find  last item then call view model method[fetchNewsHeadlines()] to fetch data form network data
    private fun setScrollListener() {
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (linearLayoutManager != null) {
                    val findFirstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    val visibleProductCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount

                    if (isScrolling && findFirstVisibleItemPosition.plus(visibleProductCount) == totalItemCount) {
                        isScrolling = false
                        viewModel.fetchNewsHeadlines(newsSource?.id!!)
                    }
                }

            }
        }
        binding.recyclerView.addOnScrollListener(listener)
    }

}