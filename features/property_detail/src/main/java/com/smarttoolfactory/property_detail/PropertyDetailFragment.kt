package com.smarttoolfactory.property_detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.property_detail.databinding.FragmentPropertyDetailBinding

class PropertyDetailFragment : DynamicNavigationFragment<FragmentPropertyDetailBinding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_property_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(requireContext(), "Property DFM Module Fragment", Toast.LENGTH_SHORT).show()
    }
}
