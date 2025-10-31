package com.cypress.bbcnewsapplication.commonComposables


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.cypress.bbcnewsapplication.R

@Composable
fun TitleIconComposable(){

//    AsyncImage(
//        model = ImageRequest.Builder(context)
//            .data(null)
//            .crossfade(true)
//            .memoryCachePolicy(CachePolicy.ENABLED)
//            .diskCachePolicy(CachePolicy.ENABLED)
//            .build(),
//        contentDescription = "source image",
//        modifier = Modifier.size(48.dp),
//        contentScale = ContentScale.Crop,
//        placeholder = painterResource(android.R.drawable.ic_menu_report_image),
//        error = painterResource(android.R.drawable.ic_menu_report_image)
//    )

    Image(
        painter = painterResource(R.drawable.ic_bbc_news),
        contentDescription = "Source image",
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape), // ← corner radius
        contentScale = ContentScale.Crop
    )
}