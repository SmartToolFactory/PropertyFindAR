package com.smarttoolfactory.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemClazz
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.dashboard.adapter.BarChartAdapter
import com.smarttoolfactory.dashboard.adapter.GridListWithTitleAdapter
import com.smarttoolfactory.dashboard.adapter.HorizontalListWithTitleAdapter
import com.smarttoolfactory.dashboard.adapter.viewholder.BarChartViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.GridSectionViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalSectionViewBinder
import com.smarttoolfactory.dashboard.databinding.FragmentDashboardBinding
import com.smarttoolfactory.dashboard.di.DaggerDashboardComponent
import com.smarttoolfactory.dashboard.di.withFactory
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardFragment :
    DynamicNavigationFragment<FragmentDashboardBinding>() {
    @Inject
    lateinit var dashboardViewModelFactory: DashboardViewModelFactory

    private val viewModel: DashboardViewModel
    by viewModels { withFactory(dashboardViewModelFactory) }

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

    private lateinit var concatAdapter: ConcatAdapter

    private lateinit var adapterFavoriteProperties: HorizontalListWithTitleAdapter
    private lateinit var adapterFavoriteChart: BarChartAdapter

    private lateinit var adapterMostViewedProperties: HorizontalListWithTitleAdapter
    private lateinit var adapterMostViewedChart: BarChartAdapter

    private lateinit var adapterRecommendedProperties: GridListWithTitleAdapter

    private lateinit var favoriteViewBinder: HorizontalSectionViewBinder
    private lateinit var viewedMostViewBinder: HorizontalSectionViewBinder

    companion object {
        private const val BUNDLE_KEY_FAVORITES_LAYOUT_MANAGER_STATE =
            "favorites_layout_manager"
        private const val BUNDLE_KEY_VIES_LAYOUT_MANAGER_STATE =
            "views_layout_manager"
        private const val BUNDLE_KEY_RECOMMENDED_LAYOUT_MANAGER_STATE =
            "recommended_layout_manager"
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//
//        if (::favoriteViewBinder.isInitialized) {
//            outState.putParcelable(
//                BUNDLE_KEY_FAVORITES_LAYOUT_MANAGER_STATE,
//                favoriteViewBinder.recyclerViewManagerState
//            )
//        }
//        super.onSaveInstanceState(outState)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)

//        createAdapters(savedInstanceState)
    }

    private fun createAdapters(savedInstanceState: Bundle?) {

        // TODO set ViewBinders for MultipleLayout Adapters
        val viewBindersPropertyFavorites = HashMap<ItemClazz, ItemBinder>()

        val scrollStateFavorite = viewModel.favoriteScrollState.value

//        favoriteViewBinder = HorizontalSectionViewBinder(
//            onItemClick = viewModel::onItemClick,
//            onSeeAllClick = viewModel::onSeeAllClick,
//            savedInstanceState?.getParcelable(
//                BUNDLE_KEY_FAVORITES_LAYOUT_MANAGER_STATE
//            )
//        )

        favoriteViewBinder = HorizontalSectionViewBinder(
            onItemClick = viewModel::onItemClick,
            onSeeAllClick = viewModel::onSeeAllClick,
            recyclerViewManagerState = scrollStateFavorite
        )

        viewedMostViewBinder = HorizontalSectionViewBinder(
            onItemClick = viewModel::onItemClick,
            onSeeAllClick = viewModel::onSeeAllClick,
            recyclerViewManagerState = savedInstanceState?.getParcelable(
                BUNDLE_KEY_VIES_LAYOUT_MANAGER_STATE
            )
        )

        /*
            Favorites
         */
        val scrollStateFavoritesFlow = MutableStateFlow(0)

        adapterFavoriteProperties =
            HorizontalListWithTitleAdapter(favoriteViewBinder)

        val chartItemClickLambda: ((Float) -> Unit) = { position ->
            scrollStateFavoritesFlow.value = position.toInt()
        }

        val favoriteChartViewBinder = BarChartViewBinder(chartItemClickLambda)
        adapterFavoriteChart = BarChartAdapter(favoriteChartViewBinder)

        /*
            Most Viewed
         */

        val scrollStateMostViewedFlow = MutableStateFlow(0)

        adapterMostViewedProperties =
            HorizontalListWithTitleAdapter(viewedMostViewBinder)

        val chartMostViewedClick: ((Float) -> Unit) = { position ->
            scrollStateMostViewedFlow.value = position.toInt()
        }

        val mostViewedChartViewBinder = BarChartViewBinder(chartMostViewedClick)
        adapterMostViewedChart = BarChartAdapter(mostViewedChartViewBinder)

        /*
            Recommended
         */
        val gridSectionViewBinder = GridSectionViewBinder(
            viewModel,
            viewModel::onItemClick
        )
        adapterRecommendedProperties = GridListWithTitleAdapter(gridSectionViewBinder)

        concatAdapter =
            ConcatAdapter(
                adapterFavoriteProperties,
                adapterFavoriteChart,
                adapterMostViewedProperties,
                adapterMostViewedChart,
                adapterRecommendedProperties
            )
    }

    override fun bindViews(view: View, savedInstanceState: Bundle?) {

        viewModel.getFavoriteProperties()
        viewModel.getFavoriteChartItems()
        viewModel.getMostViewedProperties()
        viewModel.getMostViewedChartItems()
        viewModel.getRecommendedProperties()

        dataBinding.recyclerView.visibility = View.GONE
        dataBinding.progressBar.visibility = View.VISIBLE

        createAdapters(savedInstanceState)

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

        subscribeFavorites(adapterFavoriteProperties, adapterFavoriteChart)
        subscribeMostViewed(adapterMostViewedProperties, adapterMostViewedChart)

        subscribeRecommendedProperties(adapterRecommendedProperties)

        subscribeEvents()
    }

    private fun subscribeFavorites(
        adapter: HorizontalListWithTitleAdapter,
        adapterChartAdapter: BarChartAdapter
    ) {
        viewModel.propertiesFavorite.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.LOADING -> {
                    dataBinding.recyclerView.visibility = View.GONE
                    dataBinding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapter.submitList(it.data)
                    }
                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
                else -> {

                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.chartFavoriteViewState.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.LOADING -> {
                    dataBinding.recyclerView.visibility = View.GONE
                    dataBinding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapterChartAdapter.submitList(it.data)
                    }
                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
                else -> {

                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun subscribeMostViewed(
        adapter: HorizontalListWithTitleAdapter,
        adapterChartAdapter: BarChartAdapter
    ) {
        viewModel.propertiesMostViewed.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.LOADING -> {
                    dataBinding.recyclerView.visibility = View.GONE
                    dataBinding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapter.submitList(it.data)
                    }
                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
                else -> {

                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.chartMostViewedViewState.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.LOADING -> {
                    dataBinding.recyclerView.visibility = View.GONE
                    dataBinding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapterChartAdapter.submitList(it.data)
                    }
                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
                else -> {

                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.favoriteScrollState.value = favoriteViewBinder.recyclerViewManagerState
    }

    private fun subscribeRecommendedProperties(adapter: GridListWithTitleAdapter) {
        viewModel.propertiesRecommended.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.LOADING -> {
                    dataBinding.recyclerView.visibility = View.GONE
                    dataBinding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapter.submitList(it.data)
                    }
                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
                else -> {

                    dataBinding.recyclerView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
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

                val bundle = bundleOf(
                    "propertyListModel" to propertyListModel
                )

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
}
