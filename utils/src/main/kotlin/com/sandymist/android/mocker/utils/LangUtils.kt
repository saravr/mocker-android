@file:Suppress("unused")

package com.scribd.android.mocker.utils

fun <T> noneNull(vararg elements: T) = elements.any { it == null }.not()

operator fun <T> List<T>.component6(): T = get(5)

fun Float.roundTo(n: Int): Float {
    return "%.${n}f".format(this).toFloat()
}

fun Double.roundTo(n: Int): Double {
    return "%.${n}f".format(this).toDouble()
}
