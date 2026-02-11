package com.petbulance.presentation.screen.nonfeature.login.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.utils.HtmlText

@Composable
fun TermsDetailOverlay(term: Term, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.bg.frame.default)
                .verticalScroll(rememberScrollState())
                .padding(vertical = spacingXXL, horizontal = spacingMedium)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.Close),
                    contentDescription = "Close",
                    size = iconSizeMS,
                    tint = colorScheme.icon.dark,
                    modifier = Modifier.clickable(onClick = onDismissRequest)
                )
            }
            Text(
                text = term.title,
                style = typography.titleMedium,
                color = colorScheme.text.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            HtmlText(html = term.summary)
        }
    }
}