package com.lixh.utils

import android.graphics.Bitmap
import android.view.View

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

/**
 * Created by D'Russel on 2017/7/31.
 */

object ZxingUtils {

    fun convertViewToBitmap(view: View): Bitmap {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()

        return view.drawingCache
    }

    fun createQRCode(str: String): Bitmap? {
        var bitmap: Bitmap? = null
        var result: BitMatrix? = null
        val multiFormatWriter = MultiFormatWriter()
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 400, 400)
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.createBitmap(result!!)
        } catch (e: WriterException) {
            e.printStackTrace()
        } catch (iae: IllegalArgumentException) {
            return null
        }

        return bitmap
    }

}