package com.danluk.whis

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danluk.whis.components.AuthButton
import com.danluk.whis.components.InputField

@Composable
@Preview
fun AuthorizationScreen() {
    val context = LocalContext.current
    val viewModel: WHISViewModel = viewModel(LocalContext.current as ComponentActivity)
    val user = viewModel.user

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(17.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 50.dp)
        ) {
            val usernameInitError = InputField("Username", username)
            Spacer (modifier = Modifier.height(25.dp))
            val passwordInitError = InputField("Password", password)
            Spacer (modifier = Modifier.height(75.dp))

            Row {
                AuthButton("Log In",true) {
                    if (username.value.isBlank() || password.value.isBlank()) {
                        return@AuthButton
                    }

                    user.logIn(context, username.value, password.value) {
                        usernameInitError()
                        passwordInitError()
                    }
                }

                AuthButton("Sign In",false) {
                    if (username.value.isBlank() || password.value.isBlank()) {
                        return@AuthButton
                    }

                    user.signIn(context, username.value, password.value) {
                        usernameInitError()
                    }
                }
            }
        }
    }
}
