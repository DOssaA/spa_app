package com.darioossa.myspa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import myspa.composeapp.generated.resources.Res
import myspa.composeapp.generated.resources.company_name
import myspa.composeapp.generated.resources.onboarding_account_button
import myspa.composeapp.generated.resources.onboarding_account_label
import myspa.composeapp.generated.resources.onboarding_description
import myspa.composeapp.generated.resources.onboarding_services_button
import myspa.composeapp.generated.resources.onboarding_welcome
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize()
        ) {
            Column( // TODO: consider migrating to constraintlayout when it is supported in compose multiplatform
                modifier = Modifier.padding(24.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    val title = buildAnnotatedString {
                        val prefix = stringResource(Res.string.onboarding_welcome)
                        append(prefix)
                        val company = stringResource(Res.string.company_name)
                        append(company)
                        addStyle(
                            SpanStyle(color = MaterialTheme.colorScheme.secondary),
                            prefix.length, prefix.length + company.length
                        )
                    }
                    Text(
                        style = h1Style,
                        text = title
                    )
                    Text(
                        style = h6Style,
                        text = stringResource(Res.string.onboarding_description),
                        modifier = Modifier.padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { /* TODO */ }) { // TODO: separate component
                        Text(
                            style = buttonStyle,
                            text = stringResource(Res.string.onboarding_services_button)
                        )
                    }
                    Row {
                        Text(stringResource(Res.string.onboarding_account_label))
                        Text(stringResource(Res.string.onboarding_account_button))
                    }
                }
            }
        }
    }
}

val h1Style = TextStyle(fontSize = 32.sp)
val h6Style = TextStyle(fontSize = 16.sp)
val buttonStyle = TextStyle(fontSize = 18.sp)