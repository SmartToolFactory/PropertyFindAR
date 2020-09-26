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

    val dataBinding: ViewBinding get() = _dataBinding!!

    private var onCreateViewStartTime: Long = 0

    private var onViewCreatedStartTime: Long = 0

    var totalInitTime: Long = 0

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

        onCreateViewStartTime = System.currentTimeMillis()
//        println("🤣 ${this.javaClass.simpleName} #${this.hashCode()} onCreateView()")

        // Each fragment can have it's separate toolbar menu
        setHasOptionsMenu(true)

        _dataBinding =
            DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)

        /**
         *   🔥🔥 Using viewLifecycleOwner instead of this(fragment) makes sure that
         *   when this fragment is retrieved from back stack another observer is not added
         *   again, and when onDestroyView is called removes this binding to liveData
         *   since it's bound to View instead of Fragment(this).
         */
        dataBinding.lifecycleOwner = viewLifecycleOwner

        val rootView = dataBinding.root

        // Get width and height of the fragment
        rootView.post {
            dimensions.x = rootView.width
            dimensions.y = rootView.height
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedStartTime = System.currentTimeMillis()

        println(
            "🍏  ${this.javaClass.simpleName} #${this.hashCode()}  onViewCreated() " +
                "START took ${onViewCreatedStartTime - onCreateViewStartTime} ms"
        )

        bindViews(view, savedInstanceState)

        println(
            "🍏  ${this.javaClass.simpleName} #${this.hashCode()}  onViewCreated() " +
                "FINISH took ${System.currentTimeMillis() - onCreateViewStartTime} ms"
        )
    }

    override fun onResume() {
        super.onResume()
        totalInitTime = System.currentTimeMillis() - onCreateViewStartTime
        println(
            "🍎  ${this.javaClass.simpleName} #${this.hashCode()}  onResume() " +
                "TOTAL: ${System.currentTimeMillis() - onCreateViewStartTime} ms"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
//        println("🥵 ${this.javaClass.simpleName} #${this.hashCode()}  onDestroyView()")
    }

    /**
     * Called from [Fragment.onViewCreated] to implement bound ui items and set properties
     */
    open fun bindViews(view: View, savedInstanceState: Bundle?) = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        println("😀 ${this.javaClass.simpleName} #${this.hashCode()}  onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
//        println("🥶 ${this.javaClass.simpleName} #${this.hashCode()}  onDestroy()")
    }
}
