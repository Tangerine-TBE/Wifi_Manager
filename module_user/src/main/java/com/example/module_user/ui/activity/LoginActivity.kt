package com.example.module_user.ui.activity
import android.graphics.Paint
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.provider.ModuleProvider
import com.example.module_base.utils.*
import com.example.module_user.R
import com.example.module_user.databinding.ActivityLoginBinding
import com.example.module_user.utils.Constants
import com.example.module_user.utils.NetState
import com.example.module_user.viewmodel.LoginViewModel


@Route(path = ModuleProvider.ROUTE_LOGIN_ACTIVITY)
class LoginActivity : BaseVmViewActivity<ActivityLoginBinding, LoginViewModel>(){

    companion object {
        const val REGISTER=1  //注册
        const val FIND_PWD = 2 //找回密码

    }

    override fun getViewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }
    override fun getLayoutView(): Int = R.layout.activity_login


    override fun initView() {
        binding.apply {
            setToolBar(this@LoginActivity, "", loginToolbar)
            toForgetPwd.paint.flags= Paint.UNDERLINE_TEXT_FLAG
        }

    }

    override fun initEvent() {
        binding.apply {
            loginToolbar.toolbarEvent(this@LoginActivity){}

            toRegister.setOnClickListener {
                toOtherActivity<RegisterPwdActivity>(this@LoginActivity){
                    putExtra(Constants.USER_ACTION, REGISTER)
                }
            }


            toForgetPwd.setOnClickListener {
                toOtherActivity<RegisterPwdActivity>(this@LoginActivity){
                    putExtra(Constants.USER_ACTION, FIND_PWD)
                }
            }


            toLogin.setOnClickListener {
                val number = numberInclude.text.trim().toString()
               val pwd = pwdInInclude.text.trim().toString()
               viewModel.toLocalLogin(number, pwd)
            }


        }
    }


    override fun observerData() {
        viewModel.apply {
            loginState.observe(this@LoginActivity, {
                mLoadingDialog.apply {
                    when (it.state) {
                        NetState.LOADING -> showDialog(this@LoginActivity)
                        NetState.SUCCESS -> {
                            dismiss()
                            showToast(it.msg)
                            finish()
                        }
                        NetState.ERROR -> {
                            dismiss()
                            showToast(it.msg)
                        }
                    }
                }
            })

        }
    }



}