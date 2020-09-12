package com.vastausf.wesolient.presentation.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vastausf.wesolient.R

class MainActivity : AppCompatActivity() {
    @Composable
    fun header() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalGravity = Alignment.CenterHorizontally
        ) {
            Row {
                Image(
                    vectorResource(R.drawable.ic_app)
                )
                Text(
                    "Wesolient",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
    }

    @Composable
    fun desktopList() {
        LazyColumnFor(items = (1..1000).toList()) {
            Text(text = "Item $it", Modifier.fillMaxWidth())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colors = lightColors(
                    primary = Color(0xFFFF5252),
                    primaryVariant = Color(0xFFF44336),
                    secondary = Color(0xFF03DAC5),
                    background = Color(0xFFFFFFFF)
                )
            ) {
                Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
                    header()
                    desktopList()
                }
            }
        }
    }
}