package com.lixh.rxhttp.view

import android.content.Context
import android.content.DialogInterface
import com.lixh.BuildConfig
import com.lixh.R
import com.lixh.rxhttp.exception.ApiException
import com.lixh.utils.Alert
import com.lixh.utils.Global
import com.lixh.utils.UNetWork


open class ApiProgress<T>(context: Context, msg: String?, showDialog: Boolean) : RequestState<T> {

    constructor(context: Context)
            : this(context, Global.get()?.string(R.string.loading), true)


    constructor(context: Context, showDialog: Boolean) : this(context, Global.get()?.string(R.string.loading), showDialog)

    private val showDialog = showDialog
    private val context = context
    private val msg = msg


    override fun onStart() {
        if (showDialog) {
            try {
                showProgressDialog()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     *
     * 显示Dialog
     *
     */

    private fun showProgressDialog() {
        if (showDialog) {
            try {
                Alert.displayLoading(context, R.layout.alert_proress, DialogInterface.OnDismissListener { cancelProgress() })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onSuccess(t: T) {
    }

    override fun onError(e: Throwable) {
        if (showDialog) Alert.dismiss()
        if (BuildConfig.LOG_DEBUG) e.printStackTrace()

        //网络
        //服务器
        //其它
        when {
            !UNetWork.isNetworkAvailable(context) -> onFail(403, context.getString(R.string.no_net))
            e is ApiException -> onFail(e.code, e.message)
            else -> onFail(400, context.getString(R.string.net_error))
        }

    }

    fun onFail(code: Int, message: String?) {
        message?.let { UToast.showShort("$code----$it") }
    }


    private fun cancelProgress() {

    }

    override fun onComplete() {
        if (this.showDialog) {
            Alert.dismiss()
        }
    }
}