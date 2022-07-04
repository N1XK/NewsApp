package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsPagingAdapter
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SearchNewsFragment"

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsPagingAdapter: NewsPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        var job: Job? = null
        binding.tiSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(this.viewLifecycleOwner) {
            newsPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.rvSearchNews.layoutManager?.onSaveInstanceState()?.let {
            viewModel.saveRecyclerViewState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.stateInitialized()) {
            binding.rvSearchNews.layoutManager?.onRestoreInstanceState(
                viewModel.restoreRecyclerViewState()
            )
        }
    }

    private fun hideProgessBar() {
        binding.paginationProgessBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgessBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsPagingAdapter = NewsPagingAdapter { article ->
            val action =
                SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(
                    article, TAG
                )
            this.findNavController().navigate(action)
        }
        binding.rvSearchNews.apply {
            adapter = newsPagingAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }
}

//viewModel.searchNews.observe(this.viewLifecycleOwner) { response ->
//            when(response) {
//                is Resource.Success -> {
//                    hideProgessBar()
//                    response.data?.let { newsResponse ->
//                        newsAdapter.submitList(newsResponse.articles)
//                    }
//                }
//                is Resource.Error -> {
//                    hideProgessBar()
//                    response.message?.let { message ->
//                        Toast.makeText(
//                            requireContext(), "An error occured: $message", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//                is Resource.Loading -> {
//                    showProgressBar()
//                }
//            }
//        }