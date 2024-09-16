package com.lib.quad.logixs.loadify

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter

/**
 * @author: Muhammad Kamran
 *
 */
@Composable
fun rememberAsyncImagePainterWithPlaceholder(
    model: Any,
    placeholder: Painter?=null,
    error: Painter?=null
): Painter {
    return rememberAsyncImagePainter(
        model = model,
        placeholder = placeholder,
        error = error
    )
}