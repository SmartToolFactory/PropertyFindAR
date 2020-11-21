package com.smarttoolfactory.property_detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.transition.MaterialContainerTransform
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.viewmodel.NavControllerViewModel
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.property_detail.databinding.FragmentPropertyDetailBinding
import com.smarttoolfactory.property_detail.di.DaggerPropertyDetailComponent
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class PropertyDetailFragment : DynamicNavigationFragment<FragmentPropertyDetailBinding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_property_detail

    @Inject
    lateinit var viewModel: PropertyDetailViewModel

    /**
     * ViwModel for getting [NavController] for setting Toolbar navigation
     */
    private val navControllerViewModel by activityViewModels<NavControllerViewModel>()

    lateinit var propertyItem: PropertyItem

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)

        val args = requireArguments()
        propertyItem = PropertyDetailFragmentArgs.fromBundle(args).propertyArgs
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prepareSharedElementTransition()
        postponeEnterTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun bindViews(view: View, savedInstanceState: Bundle?) {

        // Get Post from navigation component arguments
        dataBinding.item = propertyItem
        dataBinding.cardView.transitionName = propertyItem.transitionName

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.placeholder)

        Glide
            .with(view.context)
            .setDefaultRequestOptions(requestOptions)
            .load(propertyItem.thumbnail)
            .into(dataBinding.ivHeader)

        view.doOnNextLayout {
            (it.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    private fun prepareSharedElementTransition() {

        sharedElementEnterTransition = MaterialContainerTransform()
            .apply {
                duration = 500
                // Scope the transition to a view in the hierarchy so we know it will be added under
                // the bottom app bar but over the elevation scale of the exiting HomeFragment.
                // TODO This causing error find correct view or remove
//                drawingViewId = R.id.cardView
                scrimColor = Color.TRANSPARENT
//                setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
            }
    }

    private fun initCoreDependentInjection() {

        val coreModuleDependencies = EntryPointAccessors.fromApplication(
            requireActivity().applicationContext,
            CoreModuleDependencies::class.java
        )

        DaggerPropertyDetailComponent.factory().create(
            dependentModule = coreModuleDependencies,
            fragment = this
        )
            .inject(this)
    }
}
