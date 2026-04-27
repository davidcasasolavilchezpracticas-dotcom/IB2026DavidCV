package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing

@Preview(showBackground = true)
@Composable
fun LegalTextItem(title: Int = 0, description: Int = 0) {
    Column(
        modifier = Modifier
            .padding(
                vertical = LocalSpacing.current.xs
            )
    ) {
        Text(
            text = buildAnnotatedString {
                append("${stringResource(title)}: ")
                append(stringResource(description))
                append(" ")
                withStyle(style = SpanStyle(color = Color(0xFF006633), textDecoration = TextDecoration.Underline)) {
                    append(stringResource(R.string.moreInfo))
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
        )
    }
}