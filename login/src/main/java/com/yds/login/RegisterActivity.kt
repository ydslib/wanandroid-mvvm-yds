package com.yds.login

import android.text.InputType
import android.widget.ImageView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.yds.login.databinding.ActivityRegisterBinding
import com.yds.login.vm.LoginViewModel

@Route(path = RouterPath.REGISTER_ACTIVITY)
class RegisterActivity : DataBindingActivity<ActivityRegisterBinding, LoginViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_register)
    }

    override fun initObser() {
        super.initObser()
        mViewModel.registerData.observe(this) {
            UserInfo.mUserName = it.username
            UserInfo.mLoginState = true
            finish()
        }
    }

    override fun initData() {
        super.initData()
        mBinding?.seeImg?.setOnClickListener {
            mViewModel.eyeCanSeeLiveModel.value = !(mViewModel.eyeCanSeeLiveModel.value ?: false)
            if (mViewModel.eyeCanSeeLiveModel.value == true) {
                Glide.with(this@RegisterActivity).load(R.drawable.ic_password_can_see)
                    .into(it as ImageView)
                mBinding?.password?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                Glide.with(this@RegisterActivity).load(R.drawable.ic_password_not_can_see)
                    .into(it as ImageView)
                mBinding?.password?.inputType =
                    (InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT)
            }
            mBinding?.password?.setSelection(mBinding?.password?.text?.toString()?.length ?: 0)
        }

        mBinding?.seeImgAgain?.setOnClickListener {
            mViewModel.confirmPwdEyeLiveModel.value =
                !(mViewModel.confirmPwdEyeLiveModel.value ?: false)
            if (mViewModel.confirmPwdEyeLiveModel.value == true) {
                Glide.with(this@RegisterActivity).load(R.drawable.ic_password_can_see)
                    .into(it as ImageView)
                mBinding?.passwordAgain?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                Glide.with(this@RegisterActivity).load(R.drawable.ic_password_not_can_see)
                    .into(it as ImageView)
                mBinding?.passwordAgain?.inputType =
                    (InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT)
            }
            mBinding?.passwordAgain?.setSelection(
                mBinding?.passwordAgain?.text?.toString()?.length ?: 0
            )
        }

        mBinding?.registerBtn?.setOnClickListener {
            if (mBinding?.password?.text?.toString().isNullOrEmpty() ||
                mBinding?.passwordAgain?.text?.toString().isNullOrEmpty() ||
                mBinding?.email?.text?.toString().isNullOrEmpty()
            ) {
                Toast.makeText(
                    this@RegisterActivity,
                    R.string.password_or_username_not_empty,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (mBinding?.password?.text?.toString() != mBinding?.passwordAgain?.text?.toString()) {
                Toast.makeText(
                    this@RegisterActivity,
                    R.string.password_not_match,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            mViewModel.register(
                mBinding?.email?.text?.toString() ?: "",
                mBinding?.password?.text?.toString() ?: "",
                mBinding?.passwordAgain?.text?.toString() ?: ""
            )
        }
    }
}