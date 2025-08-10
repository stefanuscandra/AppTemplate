package com.template.apptemplate.utils

import android.icu.text.DecimalFormat
import java.math.BigDecimal

fun String.formatDecimal(): String {

    val bd = try {
        BigDecimal(this)
    } catch (e: NumberFormatException) {
        return this
    }

    val formatter = if (bd.stripTrailingZeros().scale() > 0) {
        DecimalFormat("###,###,###.##")
    } else {
        DecimalFormat("###,###,###.00")
    }
    return formatter.format(bd)
}
