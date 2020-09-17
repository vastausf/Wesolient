package com.vastausf.wesolient.presentation.ui.activity.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope.weight
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.vastausf.wesolient.R
import com.vastausf.wesolient.WesolientTheme

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)

        setContent {
            val createDialogState = remember { mutableStateOf(false) }

            MaterialTheme(
                colors = lightColors(
                    primary = WesolientTheme.primary,
                    primaryVariant = WesolientTheme.primaryVariant,
                    secondary = WesolientTheme.secondary,
                    secondaryVariant = WesolientTheme.secondaryVariant,
                    background = WesolientTheme.background,
                    surface = WesolientTheme.surface,
                    error = WesolientTheme.error,
                    onPrimary = WesolientTheme.onPrimary,
                    onSecondary = WesolientTheme.onSecondary,
                    onBackground = WesolientTheme.onBackground,
                    onSurface = WesolientTheme.onSurface,
                    onError = WesolientTheme.onError
                ),
                shapes = Shapes(
                    WesolientTheme.smallShape,
                    WesolientTheme.mediumShape,
                    WesolientTheme.largeShape
                )
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    buildHeader()
                    buildDesktopList()
                    buildCreateDesktopButton(createDialogState)
                }
            }
        }
    }

    @Composable
    fun buildHeader() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
    fun buildDesktopList() {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1F, true)
                .fillMaxWidth()
        ) {
            LazyColumnFor(viewModel.desktopList.value!!) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                        }
                ) {
                    Text(
                        it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(24.dp, 0.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun buildCreateDesktopButton(createDialogState: MutableState<Boolean>) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    createDialogState.value = true
                }
        ) {
            Text("create new", color = WesolientTheme.primary)
        }

        showCreateDialog(createDialogState)
    }

    @Composable
    fun showCreateDialog(createDialogState: MutableState<Boolean>) {
        if (createDialogState.value) {
            Dialog(
                onDismissRequest = {
                    createDialogState.value = false
                },
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .preferredWidth(300.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            buildTextField(
                                TextFieldValue(""),
                                placeholder = {
                                    Text("placeholder", color = WesolientTheme.textLight)
                                }
                            )
                            buildTextField(
                                TextFieldValue(""),
                                placeholder = {
                                    Text("placeholder", color = WesolientTheme.textLight)
                                }
                            )
                        }
                    }

                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(true) {

                            }
                    ) {
                        Text(
                            "create",
                            modifier = Modifier
                                .padding(24.dp, 12.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun buildTextField(
        textFieldValue: TextFieldValue,
        placeholder: @Composable () -> Unit
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            TextField(
                textFieldValue,
                onValueChange = {},
                placeholder = placeholder,
                backgroundColor = Color.Transparent
            )
        }
    }
}
