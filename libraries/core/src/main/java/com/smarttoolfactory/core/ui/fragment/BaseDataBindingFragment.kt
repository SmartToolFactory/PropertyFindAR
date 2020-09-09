package com.smarttoolfactory.core.ui.fragment

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * BaseFragment to avoid writing data-binding code over again for each fragment.
 *
 * Generic approach forces Fragments to have specified number of ViewModels if added as generic parameter
 *
 * LifeCycle of Fragments
 *
 * * onAttach()
 * * onCreate()
 * * onCreateView() -> View is created or Fragment returned from back stack
 * * onViewCreated()
 * * onStart()
 * * onResume()
 * * onPause()
 * * onStop()
 * * onDestroyView() fragment sent to back stack / Back navigation -> onCreateView() is called
 * * onDestroy()
 * * onDetach()
 */
abstract class BaseDataBindingFragment<ViewBinding : ViewDataBinding> : Fragment() {

    private var _dataBinding: ViewBinding? = null

    var dataBinding: ViewBinding? = null

    /**
     * This method gets the layout id from the derived fragment to bind to that layout via data-binding
     */
    @LayoutRes
    abstract fun getLayoutRes(): Int

    /**
     * Point that contains width and height of the fragment.
     *
     */
    private val dimensions = Point()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Each fragment can have it's separate toolbar menu
        setHasOptionsMenu(true)

        _dataBinding =
            DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)

        dataBinding = _dataBinding!!

        /**
         *   🔥🔥 Using viewLifecycleOwner instead of this(fragment) makes sure that
         *   when this fragment is retrieved from back stack another observer is not added
         *   again, and when onDestroyView is called removes this binding to liveData
         *   since it's bound to View instead of Fragment(this).
         */
        dataBinding!!.lifecycleOwner = viewLifecycleOwner

        val rootView = dataBinding!!.root

        // Get width and height of the fragment
        rootView.post {
            dimensions.x = rootView.width
            dimensions.y = rootView.height
        }

        bindViews()

        return rootView
    }

    override fun onDestroyView() {
        _dataBinding = null
        super.onDestroyView()
    }

    /**
     * Called from [Fragment.onCreateView] to implement bound ui items and set properties
     */
    open fun bindViews() {
    }
}
