package com.jawapbo.sportistic.features.auth.sign_in

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jawapbo.sportistic.core.component.CenterAlignedRow
import com.jawapbo.sportistic.features.auth.sign_in.components.EmailAndPasswordButton
import com.jawapbo.sportistic.features.auth.sign_in.components.FacebookButton
import com.jawapbo.sportistic.features.auth.sign_in.components.GoogleButton
import com.jawapbo.sportistic.features.auth.sign_in.components.SignInDivider
import com.jawapbo.sportistic.features.auth.sign_in.components.SignInImage
import com.jawapbo.sportistic.features.auth.sign_in.components.SignInSubtitle
import com.jawapbo.sportistic.features.auth.sign_in.components.SignInTitle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    onNavigateToHome: () -> Unit,
    viewModel: SignInViewModel = koinViewModel()
) {
    Scaffold { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            SignInImage()

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignInTitle()
                SignInSubtitle()

                Spacer(Modifier.size(32.dp))

                SignInDivider()

                CenterAlignedRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FacebookButton()
                    GoogleButton {
                        viewModel.onSignInWithGoogle(it) {
                            onNavigateToHome()
                        }
                    }
                }
                EmailAndPasswordButton()
                TextButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Belum memiliki akun? Buat akun disini",
                        color = Color.White
                    )
                }
            }
        }
    }
}