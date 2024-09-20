package com.lib.quad.logixs.loadify

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author: Muhammad Kamran
 * A Composable Base Function for Display Images.
 *
 * @param image image Resource ID or URL.
 * @param colorFilter The Tint Color to applied on the image.
 * @param modifier Modifier is used for customizing the layout.
 * @param loadifyType is used for selecting the type of image to display.
 * @param alpha is used for fading the image 0.0 to 1.0.
 * @param alignment alignment is used for aligning the image within its bounds.
 * @param contentScale contentScale is used for scaling the image..
 */
@Composable
fun Loadify(
    image: Any,
    modifier: Modifier = Modifier,
    loadifyType: LoadifyType = LoadifyType.Image,
    colorFilter: ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    placeholder: Int? = null,
    errorImage: Int? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    when (loadifyType) {
        is LoadifyType.Image -> {
            LoadifyImage(
                image = image,
                modifier = modifier,
                colorFilter = colorFilter,
                alpha = alpha,
                placeholder = placeholder,
                errorImage = errorImage,
                contentScale = contentScale,
                alignment = alignment
            )

        }

        is LoadifyType.Icon -> {
            LoadifyIcon(
                icon = image,
                modifier = modifier,
                placeholder = placeholder,
                errorImage = errorImage,
            )
        }

        is LoadifyType.Lottie -> {
            if (isRawResource(context = LocalContext.current, resId = image)) {
                AnimatedPreloader(
                    rawRes = LottieCompositionSpec.RawRes(image as Int), modifier = modifier
                )
            }
        }
    }
}

private fun isRawResource(context: Context, resId: Any): Boolean {
    return try {
        if (resId is Int) {
            val resourceType = context.resources.getResourceTypeName(resId)
            resourceType == "raw"
        } else {
            Log.e("Loadify", "Provided Resource id is not a raw resource")
            false
        }
    } catch (e: Resources.NotFoundException) {
        Log.e("Loadify", "isRawResource: $e")
        false
    }
}

@Composable
internal fun LoadifyImage(
    image: Any,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null,
    alpha: Float = DefaultAlpha,
    alignment: Alignment = Alignment.Center,
    placeholder: Int? = null,
    errorImage: Int? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    when (image) {
        is Int -> LoadImageResource(
            resId = image,
            modifier = modifier,
            colorFilter = colorFilter,
            alignment = alignment,
            alpha = alpha,
            contentScale = contentScale
        )

        is String -> LoadImageFromUrl(
            imageUrl = image,
            modifier = modifier,
            placeholder = placeholder,
            errorImage = errorImage,
            colorFilter = colorFilter,
            alignment = alignment,
            alpha = alpha,
            contentScale = contentScale
        )

        is Bitmap -> LoadImageBitmap(
            bitmap = image,
            modifier = modifier,
            colorFilter = colorFilter,
            alignment = alignment,
            alpha = alpha,
            contentScale = contentScale
        )


        else -> {
            Log.e("Loadify", "unsupported type")
        }
    }
}

@Composable
internal fun LoadImageResource(
    resId: Int,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    contentScale: ContentScale = ContentScale.Fit
) {
    Image(
        painter = painterResource(id = resId),
        colorFilter = colorFilter,
        contentDescription = null,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha
    )
}

/*@Composable
internal fun LoadImageUrl(
    url: String,
    modifier: Modifier = Modifier,
    placeholder: Int? = null,
    errorImage: Int? = null,
    colorFilter: ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    contentScale: ContentScale = ContentScale.Fit
) {
    Image(
        painter = rememberAsyncImagePainterWithPlaceholder(model = url,
            placeholder = placeholder?.let { painterResource(placeholder) },
            error = errorImage?.let { painterResource(errorImage) }),
        colorFilter = colorFilter,
        contentDescription = null,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha
    )
}*/

@Composable
internal fun LoadImageBitmap(
    bitmap: Bitmap,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    contentScale: ContentScale = ContentScale.Fit
) {
    Image(
        bitmap=bitmap.asImageBitmap(),
        contentDescription = null,
        colorFilter = colorFilter,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        alpha = alpha
    )
}

@Composable
internal fun LoadifyIcon(
    icon: Any,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    placeholder: Int? = null,
    errorImage: Int? = null
) {
    when (icon) {
        is Int -> Icon(
            painter = painterResource(id = icon),
            tint = tint,
            contentDescription = null,
            modifier = modifier
        )

        is String -> LoadIconFromUrl(
            iconUrl = icon,
            modifier = modifier,
            placeholder = placeholder,
            errorImage = errorImage
        )

        is Bitmap -> Icon(
            bitmap = icon.asImageBitmap(),
            contentDescription = null,
            tint = tint,
            modifier = modifier
        )

        is LottieCompositionSpec.RawRes -> AnimatedPreloader(icon, modifier = modifier)
        else -> {
            Log.e("Loadify", "unsupported type")
        }
    }
}

@Composable
internal fun AnimatedPreloader(
    rawRes: LottieCompositionSpec.RawRes, modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(rawRes)
    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = LottieConstants.IterateForever, isPlaying = true
    )

    LottieAnimation(
        composition = composition, progress = { progress }, modifier = modifier
    )
}





@Composable
internal fun LoadImageFromUrl(
    modifier: Modifier = Modifier,
    imageUrl: String,
    placeholder: Int? = null,
    errorImage: Int? = null,
    colorFilter: ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    contentScale: ContentScale = ContentScale.Fit
) {
    // Manage the bitmap state (loading, success, or error)
    val bitmapState by produceState<Bitmap?>(initialValue = null, imageUrl) {
        value = loadImage(imageUrl)
    }
    // Display the image
    Box(modifier = modifier) {
        when {
            bitmapState != null -> {
                Image(
                    bitmap = bitmapState!!.asImageBitmap(),
                    contentDescription = "Loaded Image",
                    modifier = modifier,
                    alignment = alignment,
                    contentScale = contentScale,
                    alpha = alpha,
                    colorFilter=colorFilter
                )
            }
            else -> {
                if (bitmapState!=null && placeholder!=null){
                    Image(
                        painter = painterResource(id = placeholder),
                        contentDescription = "Placeholder or Error",
                        modifier = modifier,
                        alignment = alignment,
                        contentScale = contentScale,
                        alpha = alpha,
                        colorFilter=colorFilter
                    )
                }else if(bitmapState!=null && errorImage!=null){
                    Image(
                        painter = painterResource(id =errorImage),
                        contentDescription = "Placeholder or Error",
                        modifier = modifier,
                        alignment = alignment,
                        contentScale = contentScale,
                        alpha = alpha,
                        colorFilter=colorFilter
                    )
                }
            }
        }
    }
}

@Composable
internal fun LoadIconFromUrl(
    modifier: Modifier = Modifier,
    iconUrl: String,
    placeholder: Int? = null,
    errorImage: Int? = null,
    tint: Color = LocalContentColor.current
) {
    // Manage the bitmap state (loading, success, or error)
    val bitmapState by produceState<Bitmap?>(initialValue = null, iconUrl) {
        value = loadImage(iconUrl)
    }
    // Display the image
    Box(modifier = modifier) {
        when {
            bitmapState != null -> {
                Icon(
                    bitmap = bitmapState!!.asImageBitmap(),
                    contentDescription = "Loaded Icon",
                    modifier = modifier,
                    tint = tint
                )
            }
            else -> {
                if (bitmapState!=null && placeholder!=null){
                    Icon(
                        painter = painterResource(id = placeholder),
                        contentDescription = "Placeholder or Error",
                        modifier = modifier,
                        tint = tint
                    )
                }else if(bitmapState!=null && errorImage!=null){
                    Icon(
                        painter = painterResource(id =errorImage),
                        contentDescription = "Placeholder or Error",
                        modifier = modifier,
                        tint = tint
                    )
                }
            }
        }
    }
}

// Function to load image from URL without any library
suspend fun loadImage(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream) // Convert InputStream to Bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null on error
        }
    }
}



@Preview(name = "ImageView")
@Composable
internal fun PreviewImageCatalogue() {
    Surface {
        Column(modifier = Modifier) {
            Text(
                text = "Resource Images", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            Text(
                text = "Generic Image", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            Loadify(image = R.drawable.ic_loader)

            Text(
                text = "Network Image", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )


            Text(
                text = "Local Icon", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            LoadifyIcon(icon = R.drawable.ic_loader)
        }

    }

}

sealed class LoadifyType {
    data object Lottie : LoadifyType()
    data object Image : LoadifyType()
    data object Icon : LoadifyType()
}
