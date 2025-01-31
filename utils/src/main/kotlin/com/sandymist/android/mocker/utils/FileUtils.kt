@file:Suppress("unused")

package com.scribd.android.mocker.utils

import android.content.Context
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object FileUtils {
    private const val KB = 1024
    private const val MB: Int = KB * KB
    private const val GB: Int = KB * MB

    fun toMb(bytes: Long): Float {
        return bytes.toFloat() / MB
    }

    /**
     * Returns the given bytes in long as a formatted human readable string
     * If long bytes is equivalent to less than 1GB it is formatted as "zMB",
     * otherwise it if formatted as "zGB"
     *
     * where z shows one decimal point if the decimal point value is not zero
     *
     * @param bytes file size in bytes
     * @return the formatted human readable string for the given file size
     */
    private fun formatFileDisplaySize(context: Context, bytes: Long): String {
        val fileSizeFormat: DecimalFormat =
            NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
        fileSizeFormat.applyPattern("##0.#")
        return formatFileDisplaySize(context, bytes, fileSizeFormat)
    }

    private fun formatFileDisplaySize(context: Context, bytes: Long, format: DecimalFormat): String {
        if (bytes / GB >= 1) {
            val size: Float = bytes.toFloat() / GB
            return context.getString(R.string.n_gb, format.format(size))
        } else {
            val size: Float = bytes.toFloat() / MB
            return context.getString(R.string.n_mb, format.format(size))
        }
    }
}
