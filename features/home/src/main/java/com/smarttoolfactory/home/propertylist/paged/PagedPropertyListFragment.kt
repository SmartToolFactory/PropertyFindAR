package com.smarttoolfactory.home.propertylist.paged

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.util.EndlessScrollListener
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.observe
import com.smarttoolfactory.core.viewmodel.PropertyDetailNavigationVM
import com.smarttoolfactory.home.R
import com.smarttoolfactory.home.adapter.PropertyListAdapter
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

    lateinit var itemListAdapter: PropertyListAdapter

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

    override fun bindViews(view: View, savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel

        dataBinding.recyclerView.apply {

            // Set Layout manager
            val linearLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            this.layoutManager = linearLayoutManager

            // Set RecyclerViewAdapter
            itemListAdapter = PropertyListAdapter(
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

        itemListAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        val swipeRefreshLayout = dataBinding.swipeRefreshLayout

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

                    /*
                     * This is the navController belong to Home
                     */

                    // Alternative 1 getting grand grand parent fragment of this fragment
//                    try {
//                        val homeFragment = parentFragment?.parentFragment?.parentFragment
//
//                        (homeFragment as? HomeFragment)?.findNavController()?.navigate(
//                            R.id.action_home_dest_to_propertyDetailFragment,
//                            bundle
//                        )
//
//                    } catch (e: Exception) {
//                        findNavController()
//                            .navigate(
//                                R.id.action_propertyListFragment_to_nav_graph_property_detail,
//                                bundle
//                            )
//                    }

                    // Alternative 2 use ViewModel
                    propertyDetailNavigationVM.goToPropertyDetailFromMain.value =
                        (Event(propertyItem))
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
        dataBinding.swipeRefreshLayout.setOnRefreshListener(null)
        dataBinding.recyclerView.adapter = null
        super.onDestroyView()
    }
}
