package com.yds.main

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.yds.main.adapter.GalleryAdapter
import com.yds.main.databinding.FragmentGalleryBinding
import com.yds.main.vm.GalleryViewModel

class GalleryFragment : DataBindingFragment<FragmentGalleryBinding, GalleryViewModel>() {

    private val activityViewModel by lazy {
        getActivityScopeViewModel(GalleryViewModel::class.java)
    }

    private val adapter by lazy {
        GalleryAdapter({
            startPostponedEnterTransition()
        }, { position, view ->
            GalleryActivity.currentPosition = position
//            (exitTransition as TransitionSet).excludeTarget(view,true)
            fragmentManager?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.addSharedElement(view, view.transitionName)
                ?.replace(
                    R.id.container,
                    ImagePagerFragment(),
                    ImagePagerFragment::class.java.simpleName
                )?.addToBackStack(null)
                ?.commit()

        })
    }

    override fun createObserver() {
//        activityViewModel?.imageUriList?.observe(this) {
//            adapter.dataList.clear()
//            adapter.dataList.addAll(it)
//            adapter.notifyDataSetChanged()
//        }
        activityViewModel?.imageBitmapList?.observe(this){
            adapter.dataList.clear()
            adapter.dataList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        prepareTransitions()
        postponeEnterTransition()
        return view
    }

    override fun loadDataAfterCreate() {
        super.loadDataAfterCreate()
        scrollToPosition()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.recyclerView?.let {
            it.adapter = adapter
            if (it.layoutManager is FlexboxLayoutManager) {
                (it.layoutManager as? FlexboxLayoutManager)?.apply {
                    flexWrap = FlexWrap.WRAP
                    flexDirection = FlexDirection.ROW
                    alignItems = AlignItems.STRETCH
                }
            }
//            it.layoutManager = flexboxLayoutManager
        }
        activityViewModel?.readPic()

        mBinding?.smartRefreshLayout?.setOnRefreshListener {
            activityViewModel?.getPicData(GalleryViewModel.REFRESH)
            it.finishRefresh()
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            activityViewModel?.getPicData(GalleryViewModel.LOADMORE)
            activityViewModel?.page = (activityViewModel?.page ?: 0) + 1
            it.finishLoadMore()
        }

        mBinding?.back?.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_gallery)
    }

    private fun scrollToPosition() {
        mBinding?.recyclerView?.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                mBinding?.recyclerView?.removeOnLayoutChangeListener(this)
                val layoutManager = mBinding?.recyclerView?.layoutManager
                val viewAtPosition =
                    layoutManager?.findViewByPosition(GalleryActivity.currentPosition)
                if (viewAtPosition == null || layoutManager.isViewPartiallyVisible(
                        viewAtPosition,
                        false,
                        true
                    )
                ) {
                    mBinding?.recyclerView?.post {
                        layoutManager?.scrollToPosition(GalleryActivity.currentPosition)
                    }
                }
            }

        })
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.grid_exit_transition)
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val selectedViewHolder =
                    mBinding?.recyclerView?.findViewHolderForAdapterPosition(GalleryActivity.currentPosition)
                        ?: return
                sharedElements?.put(
                    names?.get(0) ?: "",
                    selectedViewHolder.itemView.findViewById(R.id.img)
                )
            }
        })
    }
}