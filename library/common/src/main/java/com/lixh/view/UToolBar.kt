package com.lixh.view

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat

import com.lixh.R

import java.lang.reflect.Field

class UToolBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.toolbarStyle) : Toolbar(context, attrs, defStyleAttr) {

    private var mTitleTextView: TextView? = null
    private var mSubtitleTextView: TextView? = null
    var rightIcon: ImageButton? = null
        private set
    var rightTextView: TextView? = null
        private set
    private var mLogoView: ImageView? = null
    internal var mNavButtonView: ImageButton? = null

    val menus: ActionMenuView

    var gravity = Gravity.LEFT

    val statusBarHeight: Int
        get() {
            val statusBarHeight = Math.ceil((25 * context.resources.displayMetrics.density).toDouble())
            return statusBarHeight.toInt()
        }

    val customView: View
        get() {
            (context as AppCompatActivity).supportActionBar!!.setDisplayShowCustomEnabled(true)
            return (context as AppCompatActivity).supportActionBar!!.customView
        }

    override fun setLogo(@DrawableRes resId: Int) {
        super.setLogo(resId)

        try {
            mLogoView = get("mLogoView") as ImageView?
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

    }

    /**
     * 设置阴影
     *
     * @param elevation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun setElevation(elevation: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.setElevation(elevation)
        }

    }

    /**
     * tittle的显示必须为 false 不然次不管用
     *
     * @param isEnabled
     * @return
     */
    fun setDisplayShowTitleEnabled(isEnabled: Boolean): UToolBar {
        (context as AppCompatActivity).setSupportActionBar(this)
        (context as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(isEnabled)
        return this
    }

    fun setCustomView(view: View, layoutParams: Toolbar.LayoutParams): UToolBar {
        //显示自定义视图
        (context as AppCompatActivity).supportActionBar!!.setDisplayShowCustomEnabled(true)
        (context as AppCompatActivity).supportActionBar!!.setCustomView(view, layoutParams)
        return this
    }

    fun setCustomView(view: View): UToolBar {
        (context as AppCompatActivity).supportActionBar!!.setDisplayShowCustomEnabled(true)
        (context as AppCompatActivity).supportActionBar!!.customView = view
        return this
    }

    fun setCustomView(resId: Int): UToolBar {
        (context as AppCompatActivity).supportActionBar!!.setDisplayShowCustomEnabled(true)
        (context as AppCompatActivity).supportActionBar!!.setCustomView(resId)
        return this
    }

    /**
     * 是否显示返回 默认带返回按钮 子类如果为false
     * 将不显示返回按钮
     *
     * @param isShow
     * @return
     */
    fun setDisplayHomeAsUpEnabled(isShow: Boolean): UToolBar {
        (context as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(isShow)
        setNavigationOnClickListener { (context as AppCompatActivity).onBackPressed() }
        return this
    }

    /**
     * 设置返回按钮
     *
     * @param id
     * @param onClickListener
     */
    fun setNavigationIcon(id: Int, onClickListener: View.OnClickListener) {
        setNavigationIcon(id)
        setNavigationOnClickListener(onClickListener)
    }

    init {
        menus = ActionMenuView(getContext())
        menus.layoutParams = getLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT)
        ViewCompat.setLayoutDirection(menus, ViewCompat.LAYOUT_DIRECTION_RTL)
        set("mButtonGravity", Gravity.CENTER_VERTICAL)

    }

    /**
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    fun getLayoutParams(width: Int, height: Int, gravity: Int): Toolbar.LayoutParams {
        return Toolbar.LayoutParams(width, height, gravity)

    }

    fun setHasBar() {
        minimumHeight = suggestedMinimumHeight + statusBarHeight
        setPadding(0, statusBarHeight, 0, 0)
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)
        mTitleTextView = getTitleValue(title, "mTitleTextView")
    }

    override fun setSubtitle(subtitle: CharSequence) {
        super.setSubtitle(subtitle)
        mSubtitleTextView = getTitleValue(subtitle, "mSubtitleTextView")
    }

    override fun setNavigationIcon(icon: Drawable?) {
        super.setNavigationIcon(icon)
    }

    fun setNavigationIcon(@DrawableRes resId: Int, backStr: String) {
        try {
            mNavButtonView = get("mNavButtonView") as ImageButton?
            val bitmap = drawTextToBitmap(resId, backStr)
            mNavButtonView!!.setImageBitmap(bitmap)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

    }

    fun drawTextToBitmap(@DrawableRes icon: Int, text: String): Bitmap {
        val resources = resources
        val scale = resources.displayMetrics.density
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        paint.textSize = (14 * scale).toInt().toFloat()
        val bitmap = BitmapFactory.decodeResource(resources, icon)
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val canvasBmp = Bitmap.createBitmap(bitmap.width + bounds.width(), bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(canvasBmp)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.drawText(text, bitmap.width.toFloat(), ((bitmap.height + bounds.height()) / 2).toFloat(), paint)
        return canvasBmp
    }


    fun getTitleValue(tittle: CharSequence, fieldName: String): TextView? {
        var childTitle: TextView? = null
        try {
            childTitle = get(fieldName) as TextView?

        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        return childTitle
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (gravity == Gravity.CENTER_HORIZONTAL) {
            setCenter(mTitleTextView)
            setCenter(mSubtitleTextView)
            setLogoViewCenter(mLogoView)
        }
    }

    /**
     * 增加多个按钮
     *
     * @param view
     */
    fun addActionMenu(view: View) {
        removeView(menus)
        menus.addView(view)
        addView(menus)
    }

    /**
     * 增加多个按钮
     *
     * @param view
     */
    fun addActionMenu(vararg view: View) {
        removeView(menus)
        for (v in view) {
            menus.addView(v)
        }
        addView(menus)
    }

    /**
     * 右边图片
     *
     * @param resId
     * @param id    资源id
     */
    fun setRightImage(resId: Int, id: Int) {
        createRightImage(resId, null, 0)
    }

    /**
     * 右边图片 监听
     *
     * @param resId
     */
    fun setRightImage(resId: Int, clickListener: View.OnClickListener) {
        createRightImage(resId, clickListener, 0)
    }

    /**
     * @param resId 默认id=0
     */
    fun setRightImage(resId: Int) {
        createRightImage(resId, null, 0)
    }

    /**
     * 添加文字
     *
     * @param str
     * @param clickListener 监听
     */
    fun setRightText(str: String, clickListener: View.OnClickListener) {
        createRightText(str, clickListener, 1)
    }

    /**
     * 添加文字
     *
     * @param str
     * @param id  资源id
     */
    fun setRightText(str: String, id: Int) {
        createRightText(str, null, id)
    }

    /**
     * 添加文字
     *
     * @param str 默认id=1
     */
    fun setRightText(str: String) {
        createRightText(str, null, 1)
    }

    private fun createRightImage(resId: Int, clickListener: View.OnClickListener?, id: Int) {
        if (resId == 0) return
        if (rightIcon == null) {
            rightIcon = AppCompatImageButton(context, null,
                    R.attr.toolbarNavigationButtonStyle)
            rightIcon!!.id = id
            rightIcon!!.setImageResource(resId)
            rightIcon!!.setOnClickListener(clickListener)
            menus.addView(rightIcon)
            removeView(menus)
            addView(menus)
        }
    }

    private fun createRightText(str: String, clickListener: View.OnClickListener?, id: Int) {
        if (!TextUtils.isEmpty(str)) {
            if (rightTextView == null) {
                val context = context
                rightTextView = AppCompatTextView(context)
                rightTextView!!.setSingleLine()
                rightTextView!!.ellipsize = TextUtils.TruncateAt.END
                rightTextView!!.text = str
                rightTextView!!.id = id
                rightTextView!!.setPadding(0, 0, 16, 0)
                rightTextView!!.setOnClickListener(clickListener)
                menus.addView(rightTextView)
                removeView(menus)
                addView(menus)
            }

        }
    }


    fun setLogoViewCenter(logoViewCenter: ImageView?) {

        val deviceWidth = measuredWidth
        var tx = deviceWidth.toFloat()
        if (logoViewCenter == null) {
            return
        }
        tx = (tx - logoViewCenter.measuredWidth) / 2.0f
        if (mTitleTextView != null) {
            val p = mTitleTextView!!.paint
            val textWidth = p.measureText(mTitleTextView!!.text.toString())
            if (textWidth != 0f) {
                tx = (deviceWidth - textWidth) / 2.0f - mTitleTextView!!.left
            }
        }
        if (mSubtitleTextView != null) {
            val p = mSubtitleTextView!!.paint
            val textWidth = p.measureText(mSubtitleTextView!!.text.toString())
            if (textWidth != 0f) {
                tx = Math.min(tx, (deviceWidth - textWidth) / 2.0f - mSubtitleTextView!!.left)
            }
        }

        logoViewCenter.translationX = tx
    }

    fun setCenter(childTitle: TextView?) {
        if (childTitle == null) {
            return
        }
        val deviceWidth = measuredWidth
        val p = childTitle.paint
        val textWidth = p.measureText(childTitle.text.toString())
        val tx = (deviceWidth - textWidth) / 2.0f - childTitle.left
        childTitle.translationX = tx
    }

    operator fun set(variableName: String, value: Any) {
        val targetClass = javaClass.superclass
        try {
            val field = targetClass!!.getDeclaredField(variableName)
            field.isAccessible = true
            field.set(this, value)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

    }

    @Throws(NoSuchFieldException::class)
    operator fun get(variableName: String): Any? {
        val targetClass = javaClass.superclass
        val superInst = targetClass!!.cast(this) as Toolbar?
        val field: Field
        try {
            field = targetClass.getDeclaredField(variableName)
            //修改访问限制
            field.isAccessible = true
            return field.get(superInst)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun setOnMenuItemClickListener(onClickListener: View.OnClickListener) {
        for (i in 0 until menus.childCount) {
            menus.getChildAt(i).setOnClickListener(onClickListener)
        }
    }
}