package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.arthur.hadithifilmsandphotography.R
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GalleryScreen(navController: NavHostController) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val imageList = listOf(
        Pair(painterResource(id = R.drawable.wedding), "Wedding Moments"),
        Pair(painterResource(id = R.drawable.graduation), "Graduation"),
        Pair(painterResource(id = R.drawable.corporate), "Corporate"),
        Pair(painterResource(id = R.drawable.personalshoots), "Personal Shoot"),
        Pair(painterResource(id = R.drawable.birthday), "Birthday shoot"),
        Pair(painterResource(id = R.drawable.babybump), "Baby Bump"),

    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(
                count = imageList.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = imageList[page].first,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = imageList[page].second,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val prevPage = (pagerState.currentPage - 1).coerceAtLeast(0)
                            pagerState.animateScrollToPage(prevPage)
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val nextPage = (pagerState.currentPage + 1).coerceAtMost(imageList.lastIndex)
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                }
            }
        }
Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Gallery",
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier.padding(top = 16.dp)
        )

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryScreenPreview() {
    GalleryScreen(navController = rememberNavController())
}
