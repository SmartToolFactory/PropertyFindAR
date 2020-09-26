package com.smarttoolfactory.home.propertylist.rxjava

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.observe
import com.smarttoolfactory.core.viewmodel.PropertyDetailNavigationVM
import com.smarttoolfactory.home.R
import com.smarttoolfactory.home.adapter.PropertyListAdapter
import com.smarttoolfactory.home.databinding.FragmentPropertyListBinding
import com.smarttoolfactory.home.di.DaggerHomeComponent
import com.smarttoolfactory.home.viewmodel.HomeToolbarVM
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class PropertyListFragmentRxJava3 : DynamicNavigationFragment<FragmentPropertyListBinding>() {

    @Inject
    lateinit var viewModel: PropertyListViewModelRxJava3

    private val propertyDetailNavigationVM by activityViewModels<PropertyDetailNavigationVM>()

    lateinit var itemListAdapter: PropertyListAdapter

    /**
     * ViewModel for setting sort filter on top menu and property list fragments
     */
    private val toolbarVM by activityViewModels<HomeToolbarVM>()

    override fun getLayoutRes(): Int = R.layout.fragment_property_list

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
    }

    override fun bindViews(view: View, savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel

        // Fetch offline-first data
        viewModel.getPropertyList()

        dataBinding.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            // Set RecyclerViewAdapter
            itemListAdapter = PropertyListAdapter(
                R.layout.item_property_list,
                viewModel::onClick,
                viewModel::onLikeButtonClick

            )

            itemListAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            this.adapter = itemListAdapter
        }

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
                    /*
                        Navigates from Main fragment of the app which replaces everything with
                        detail
                     */
                    propertyDetailNavigationVM.goToPropertyDetailFromHome.value =
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

    override fun onDestroyView() {
        dataBinding.swipeRefreshLayout.setOnRefreshListener(null)
        super.onDestroyView()
    }
}
