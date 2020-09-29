package com.smarttoolfactory.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.di.qualifier.RecycledViewPool
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemClazz
import com.smarttoolfactory.core.ui.recyclerview.adapter.MappableItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleViewBinderAdapter
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.dashboard.adapter.viewholder.ChartSectionViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalSectionViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.LoadingIndicator
import com.smarttoolfactory.dashboard.adapter.viewholder.LoadingViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.RecommendedSectionViewBinder
import com.smarttoolfactory.dashboard.databinding.FragmentDashboardBinding
import com.smarttoolfactory.dashboard.di.DaggerDashboardComponent
import com.smarttoolfactory.dashboard.di.withFactory
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DashboardFragment :
    DynamicNavigationFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var dashboardViewModelFactory: DashboardViewModelFactory

    @RecycledViewPool(RecycledViewPool.Type.CHART_ITEM)
    @Inject
    lateinit var chartPool: RecyclerView.RecycledViewPool

    @RecycledViewPool(RecycledViewPool.Type.PROPERTY_HORIZONTAL)
    @Inject
    lateinit var propertyPool: RecyclerView.RecycledViewPool

    private val viewModel: DashboardViewModel
    by viewModels { withFactory(dashboardViewModelFactory) }

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

    private lateinit var concatAdapter: ConcatAdapter

    val viewBinders = HashMap<ItemClazz, MappableItemBinder>()

    private lateinit var adapterFavoriteProperties: SingleViewBinderAdapter
    private lateinit var adapterFavoriteChart: SingleViewBinderAdapter

    private lateinit var adapterMostViewedProperties: SingleViewBinderAdapter
    private lateinit var adapterMostViewedChart: SingleViewBinderAdapter

    private lateinit var adapterRecommendedProperties: SingleViewBinderAdapter

    private lateinit var favoriteViewBinder: HorizontalSectionViewBinder
    private lateinit var viewedMostViewBinder: HorizontalSectionViewBinder
    private lateinit var recommendedViewBinder: RecommendedSectionViewBinder

    private var fetchRecommendedItems = true

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
        createAdapters(savedInstanceState)
    }

    /**
     * Creates adapters for outer and  inner recyclerViews, viewBinders to create
     * each type of ViewHolder depending on layout and data type
     */
    private fun createAdapters(savedInstanceState: Bundle?) {

        favoriteViewBinder = HorizontalSectionViewBinder(
            viewModel = viewModel,
            layoutManagerState = viewModel.scrollStateFavorites.value,
            pool = propertyPool
        )

        viewedMostViewBinder = HorizontalSectionViewBinder(
            viewModel = viewModel,
            layoutManagerState = viewModel.scrollStateMostViewed.value
        )

        recommendedViewBinder = RecommendedSectionViewBinder(
            viewModel = viewModel,
            layoutManagerState = viewModel.scrollStateRecommended.value
        )

        /*
            Favorites
         */
        adapterFavoriteProperties =
            SingleViewBinderAdapter(favoriteViewBinder as MappableItemBinder)

        val favoriteChartViewBinder = ChartSectionViewBinder()
//        { position ->
//            scrollStateFavoritesFlow.value = position.toInt()
//        }

        adapterFavoriteChart =
            SingleViewBinderAdapter(favoriteChartViewBinder as MappableItemBinder)

        /*
            Most Viewed
         */
        adapterMostViewedProperties =
            SingleViewBinderAdapter(viewedMostViewBinder as MappableItemBinder)

        val mostViewedChartViewBinder = ChartSectionViewBinder()
        adapterMostViewedChart =
            SingleViewBinderAdapter(mostViewedChartViewBinder as MappableItemBinder)

        /*
            Recommended
         */
        adapterRecommendedProperties =
            SingleViewBinderAdapter(recommendedViewBinder as MappableItemBinder)

        /*
            setIsolateViewTypes(false) lets nested adapters to use same RecycledViewPool

            🔥 Note: causing layout manager states to be overridden,
            saving state only one ViewHolder, others are returning NULL

         */
        val concatBuildConfig = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()

        concatAdapter =
            ConcatAdapter(
//                concatBuildConfig,
                adapterFavoriteProperties,
                adapterFavoriteChart,
                adapterMostViewedProperties,
                adapterMostViewedChart
            )
    }

    override fun bindViews(view: View, savedInstanceState: Bundle?) {

        viewModel.getDashboardDataCombined()

//        createAdapters(savedInstanceState)

        // Check if RecyclerView has reached the bottom
        dataBinding.recyclerView.addOnScrollListener(scrollListener)

        dataBinding.viewModel = viewModel

        dataBinding.recyclerView.apply {

            // Set Layout manager
            val linearLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            this.layoutManager = linearLayoutManager

            // Set RecyclerViewAdapter
            if (adapter == null) {
                this.adapter = concatAdapter
            }
        }

        subscribeAdapterToData(adapterFavoriteProperties, viewModel.propertiesFavorite)
        subscribeAdapterToData(adapterFavoriteChart, viewModel.chartFavoriteViewState)
        subscribeAdapterToData(adapterMostViewedProperties, viewModel.propertiesMostViewed)
        subscribeAdapterToData(adapterMostViewedChart, viewModel.chartMostViewedViewState)

        subscribeRecommendedProperties(adapterRecommendedProperties)
        subscribeEvents()

        viewModel.combinedEventData.observe(
            viewLifecycleOwner,
            {
                it.getContentIfNotHandled()?.status?.let { status ->
                    if (status == Status.LOADING) {
                        dataBinding.recyclerView.visibility = View.GONE
                        dataBinding.contentLoadingProgressBar.visibility = View.VISIBLE
                    } else {
                        dataBinding.contentLoadingProgressBar.visibility = View.GONE
                        dataBinding.recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        )
    }

    private fun <T> subscribeAdapterToData(
        adapter: SingleViewBinderAdapter,
        liveData: LiveData<ViewState<List<T>>>,
    ) {

        liveData.observe(
            viewLifecycleOwner,
            { viewState ->

                if (viewState.status == Status.SUCCESS && !viewState.data.isNullOrEmpty()) {
                    adapter.submitList(viewState.data)
                }
            }
        )
    }

    private fun subscribeRecommendedProperties(adapter: SingleViewBinderAdapter) {

        viewModel.propertiesRecommended.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.LOADING -> {

                    val loadingAdapter =
                        SingleViewBinderAdapter(LoadingViewBinder() as MappableItemBinder)
                            .apply { submitList(listOf(LoadingIndicator)) }

                    concatAdapter.addAdapter(loadingAdapter)
                    dataBinding.recyclerView
                        .smoothScrollToPosition(concatAdapter.adapters.size - 1)
                }
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        val recyclerView = dataBinding.recyclerView
                        val adapters = concatAdapter.adapters
                        if (!adapters.contains(adapter)) {

                            concatAdapter.removeAdapter(adapters[adapters.size - 1])
                            concatAdapter.addAdapter(adapter)
                            adapter.submitList(it.data)

                            if (!recyclerView.canScrollVertically(1) &&
                                recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE
                            )
                                dataBinding.recyclerView
                                    .smoothScrollToPosition(concatAdapter.adapters.size - 1)
                        }
                    }
                }
                else -> {
                    val adapters = concatAdapter.adapters
                    concatAdapter.removeAdapter(adapters[adapters.size - 1])
                }
            }
        }
    }

    private fun subscribeEvents() {

        viewModel.goToDetailScreen.observe(
            viewLifecycleOwner
        ) {
            it.getContentIfNotHandled()?.let { propertyItem ->
                val bundle = bundleOf("property" to propertyItem)
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_nav_graph_property_detail,
                    bundle
                )
            }
        }

        viewModel.goToSeeAllListScreen.observe(
            viewLifecycleOwner
        ) {
            it.getContentIfNotHandled()?.let { propertyListModel ->

                val bundle = bundleOf("propertyListModel" to propertyListModel)

                findNavController().navigate(
                    R.id.action_dashboardFragment_to_dashboardSeeAllFragment,
                    bundle
                )
            }
        }
    }

    private fun initCoreDependentInjection() {

        val coreModuleDependencies = EntryPointAccessors.fromApplication(
            requireActivity().applicationContext,
            CoreModuleDependencies::class.java
        )

        DaggerDashboardComponent.factory().create(
            dependentModule = coreModuleDependencies,
            fragment = this
        )
            .inject(this)
    }

    override fun onDestroyView() {
        if (::favoriteViewBinder.isInitialized) {
            viewModel.scrollStateFavorites.value = favoriteViewBinder.layoutManagerState
        }
        if (::viewedMostViewBinder.isInitialized) {
            viewModel.scrollStateMostViewed.value = viewedMostViewBinder.layoutManagerState
        }
        if (::recommendedViewBinder.isInitialized) {
            viewModel.scrollStateRecommended.value = recommendedViewBinder.layoutManagerState
        }

        dataBinding.recyclerView.removeOnScrollListener(scrollListener)
        dataBinding.viewModel = null

        super.onDestroyView()
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            // Check if scrolled to bottom of RecyclerView and not fetched recommended data
            if (!recyclerView.canScrollVertically(1) &&
                newState == RecyclerView.SCROLL_STATE_IDLE &&
                fetchRecommendedItems
            ) {
                viewModel.getRecommendedProperties()
                fetchRecommendedItems = false
            }
        }
    }
}
