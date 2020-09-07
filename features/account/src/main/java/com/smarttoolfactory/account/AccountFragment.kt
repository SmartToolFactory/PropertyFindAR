package com.smarttoolfactory.account

import com.smarttoolfactory.account.databinding.FragmentAccountBinding
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment

class AccountFragment : DynamicNavigationFragment<FragmentAccountBinding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_account
}
