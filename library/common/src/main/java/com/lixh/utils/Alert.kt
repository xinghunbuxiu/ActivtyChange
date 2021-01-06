package com.lixh.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import com.afollestad.materialdialogs.MaterialDialog
import com.flyco.animation.BounceEnter.BounceTopEnter
import com.flyco.animation.SlideExit.SlideBottomExit
import com.flyco.dialog.entity.DialogMenuItem
import com.flyco.dialog.listener.OnBtnClickL
import com.flyco.dialog.listener.OnOperItemClickL
import com.flyco.dialog.widget.ActionSheetDialog
import com.flyco.dialog.widget.NormalDialog
import com.flyco.dialog.widget.NormalListDialog
import java.util.*

/**
 * Created by LIXH on 2017/6/5.
 * email lixhVip9@163.com
 * des
 */

object Alert {

    private val bas_in = BounceTopEnter()
    private val bas_out = SlideBottomExit()
    private val dialogQueue = LinkedList<Dialog>()
    private var currentDialog: Dialog? = null

    private fun showDialog(dialog: Dialog?) {
        if (dialog != null) {
            dialogQueue.offer(dialog)
        }
        if (currentDialog == null) {
            currentDialog = dialogQueue.poll()
            if (currentDialog != null) {
                currentDialog!!.show()
                currentDialog!!.setOnDismissListener { dialog1 ->
                    currentDialog = null
                    showDialog(null)
                }
            }
        }
    }

    /**
     * 普通对话框
     *
     * @param context
     * @param message
     */
    fun displayAlertDialog(context: Context, message: String) {
        displayAlertDialog(context, "温馨提示", message)
    }

    /**
     * 普通对话框
     *
     * @param context
     * @param title
     * @param message
     */
    @JvmOverloads
    fun displayAlertDialog(context: Context, title: String, message: String, vararg onBtnClickL: OnBtnClickL = emptyArray()) {
        val dialog = NormalDialog(context)
        dialog.content(message)//
                .style(NormalDialog.STYLE_ONE)//
                .title(title)
                .btnNum(1)
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//
        if (onBtnClickL.isEmpty()) {
            dialog.setOnBtnClickL({ dismiss() } as OnBtnClickL)
        } else {
            dialog.setOnBtnClickL(*onBtnClickL)
        }
        showDialog(dialog)
    }

    fun displayAlertDialog(context: Context, warn: String, message: String, okStr: String, cancelStr: String, okOnClickListener: OnBtnClickL, cancelOnClickListener: OnBtnClickL?) {
        val dialog = NormalDialog(context)
        dialog.content(message)//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23f)//
                .btnNum(3)
                .btnText(okStr, cancelStr)
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//

        dialog.setOnBtnClickL(okOnClickListener,
                cancelOnClickListener)
        showDialog(dialog)
    }

    fun displayAlertSelectedDialog(context: Context, stringItems: Array<String>, onOperItemClickL: OnOperItemClickL) {
        val dialog = ActionSheetDialog(context, stringItems, null)
        dialog.isTitleShow(false)
                .setOnOperItemClickL { parent, view, position, id ->
                    dismiss()
                    onOperItemClickL.onOperItemClick(parent, view, position, id)
                }
        showDialog(dialog)
    }

    fun displayAlertSingledDialog(context: Context, stringItems: Array<String>, onOperItemClickL: OnOperItemClickL?) {
        val testItems = ArrayList<DialogMenuItem>()

        val dialog = NormalListDialog(context, stringItems)
        dialog.title("请选择")//
                .layoutAnimation(null)
        dialog.setOnOperItemClickL { parent, view, position, id ->
            dialog.dismiss()
            onOperItemClickL?.onOperItemClick(parent, view, position, id)
        }
        showDialog(dialog)
    }

    fun displayLoading(context: Context, layoutId: Int, dismissListener: DialogInterface.OnDismissListener) {
        val dialog = MaterialDialog.Builder(context)
                .customView(layoutId, false)
                .dismissListener(dismissListener).build()
        showDialog(dialog)
    }

    fun dismiss() {
        if (currentDialog != null) {
            currentDialog!!.dismiss()
        }
    }
}
