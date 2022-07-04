package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.NewsLoadStateAdapter
import com.example.newsapp.adapters.NewsPagingAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.NewsViewModelProviderFactory

private const val TAG = "BreakingNewsFragment"

class BreakingNewsFragment : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
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
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        viewModel.breakingNews().observe(viewLifecycleOwner) {
            newsPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.rvBreakingNews.layoutManager?.onSaveInstanceState()?.let {
            viewModel.saveRecyclerViewState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.stateInitialized()) {
            binding.rvBreakingNews.layoutManager?.onRestoreInstanceState(
                viewModel.restoreRecyclerViewState()
            )
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgessBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgessBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsPagingAdapter = NewsPagingAdapter { article ->
            val action =
                BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(
                    article, TAG
                )
            this.findNavController().navigate(action)
        }

        binding.rvBreakingNews.apply {
            adapter = newsPagingAdapter.apply {
            withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { newsPagingAdapter.retry() },
                footer = NewsLoadStateAdapter { newsPagingAdapter.retry() })
            }
            layoutManager = LinearLayoutManager(this.context)
        }

        binding.pullToRefresh.setOnRefreshListener {
            newsPagingAdapter.refresh()
            binding.pullToRefresh.isRefreshing = false
        }
    }
}