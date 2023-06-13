package com.yds.home

import android.view.KeyEvent
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.just.agentweb.AgentWeb
import com.yds.base.BaseDataBindingActivity
import com.yds.home.databinding.ActivityBrowserBinding

@Route(path = RouterPath.BROWSER_ACTIVITY)
class BrowserActivity : BaseDataBindingActivity<ActivityBrowserBinding, BaseViewModel>() {

    companion object {
        const val ARTICLE_URL = "article_url"
        const val ARTICLE_TITLE = "article_title"
    }

    var agentWeb: AgentWeb? = null

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_browser)
    }

    override fun initData() {
        super.initData()
        val url = intent.extras?.getString(ARTICLE_URL)
        val title = intent.extras?.getString(ARTICLE_TITLE)
        mBinding?.title?.text = title

        mBinding?.back?.setOnClickListener {
            if (agentWeb?.back() != true){
                finish()
            }
        }

        mBinding?.container?.let {
            agentWeb = AgentWeb.with(this)
                .setAgentWebParent(it, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return (agentWeb?.handleKeyEvent(keyCode, event) ?: false) || super.onKeyDown(
            keyCode,
            event
        )
    }

    override fun initObser() {
        super.initObser()
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }
}