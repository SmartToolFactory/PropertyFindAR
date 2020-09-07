package com.smarttoolfactory.favorites

import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.favorites.databinding.FragmentFavoritesBinding

class FavoritesFragment : DynamicNavigationFragment<FragmentFavoritesBinding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_favorites
}
