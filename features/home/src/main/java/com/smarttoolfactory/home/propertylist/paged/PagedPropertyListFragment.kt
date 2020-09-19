package com.smarttoolfactory.home.propertylist.paged

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.util.EndlessScrollListener
import com.smarttoolfactory.core.util.observe
import com.smarttoolfactory.core.viewmodel.PropertyDetailNavigationVM
import com.smarttoolfactory.home.R
import com.smarttoolfactory.home.adapter.PropertyItemListAdapter
import com.smarttoolfactory.home.databinding.FragmentPropertyListPagedBinding
import com.smarttoolfactory.home.di.DaggerHomeComponent
import com.smarttoolfactory.home.viewmodel.HomeToolbarVM
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class PagedPropertyListFragment :
    DynamicNavigationFragment<FragmentPropertyListPagedBinding>(),
    EndlessScrollListener.ScrollToBottomListener {

    @Inject
    lateinit var viewModel: PagedPropertyListViewModel

    private val propertyDetailNavigationVM by activityViewModels<PropertyDetailNavigationVM>()

    lateinit var itemListAdapter: PropertyItemListAdapter

    /**
     * Listener for listening scrolling to last item of RecyclerView
     */
    private lateinit var endlessScrollListener: EndlessScrollListener

    /**
     * ViewModel for setting sort filter on top menu and property list fragments
     */
    private val toolbarVM by activityViewModels<HomeToolbarVM>()

    override fun getLayoutRes(): Int = R.layout.fragment_property_list_paged

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
        viewModel.refreshPropertyList()
    }

    override fun bindViews() {
        dataBinding!!.viewModel = viewModel

        dataBinding!!.recyclerView.apply {

            // Set Layout manager
            val linearLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            this.layoutManager = linearLayoutManager

            // Set RecyclerViewAdapter
            itemListAdapter = PropertyItemListAdapter(
                R.layout.item_property_list,
                viewModel::onClick,
                viewModel::onLikeButtonClick

            )

            // Set Adapter
            this.adapter = itemListAdapter

            // Set RecyclerView layout manager, adapter, and scroll listener for infinite scrolling
            endlessScrollListener =
                EndlessScrollListener(linearLayoutManager, this@PagedPropertyListFragment)
            this.addOnScrollListener(endlessScrollListener)
        }

        val swipeRefreshLayout = dataBinding!!.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refreshPropertyList()
        }

        subscribeViewModelSortChange()

        subscribeGoToDetailScreen()
    }

    /**
     * When sort key is fetched from database change the one belong to Toolbar
     */
    private fun subscribeViewModelSortChange() {
        viewLifecycleOwner.observe(viewModel.orderKey) {
            toolbarVM.currentSortFilter = it
        }
    }

    private fun subscribeToolbarSortChange() {

        viewLifecycleOwner.observe(toolbarVM.queryBySort) {
            it.getContentIfNotHandled()?.let { orderBy ->
                viewModel.refreshPropertyList(orderBy)
                toolbarVM.currentSortFilter = orderBy
            }
        }
    }

    private fun subscribeGoToDetailScreen() {

        viewModel.goToDetailScreen.observe(
            viewLifecycleOwner,
            {

                it.getContentIfNotHandled()?.let { propertyItem ->
                    val bundle = bundleOf("property" to propertyItem)
                    findNavController().navigate(
                        R.id.action_propertyListFragment_to_nav_graph_property_detail,
                        bundle
                    )
                }
            }
        )
    }

    private fun initCoreDependentInjection() {

        val coreModuleDependencies = EntryPointAccessors.fromApplication(
            requireActivity().applicationContext,
            CoreModuleDependencies::class.java
        )

        DaggerHomeComponent.factory().create(
            dependentModule = coreModuleDependencies,
            fragment = this
        )
            .inject(this)
    }

    override fun onResume() {
        super.onResume()
        subscribeToolbarSortChange()
    }

    override fun onPause() {
        super.onPause()
        toolbarVM.queryBySort.removeObservers(viewLifecycleOwner)
    }

    override fun onScrollToBottom() {
        viewModel.getPropertyList()
    }

    override fun onDestroyView() {
        dataBinding!!.swipeRefreshLayout.setOnRefreshListener(null)
        super.onDestroyView()
    }
}
