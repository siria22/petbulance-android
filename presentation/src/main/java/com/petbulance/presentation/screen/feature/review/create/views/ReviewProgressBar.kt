package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateStep


@Composable
fun ReviewProgressBar(
    modifier: Modifier = Modifier,
    currentStep: ReviewCreateStep
) {
    val currentOrdinal = currentStep.ordinal
    val activeColor = colorScheme.action.primary.default
    val inactiveColor = colorScheme.bg.frame.subtle

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Step 1
        StepIndicator(
            state = getStepState(0, currentOrdinal),
            activeColor = activeColor,
            inactiveColor = inactiveColor
        )

        // Line 1-2
        StepLine(
            active = currentOrdinal >= 1,
            activeColor = activeColor,
            inactiveColor = inactiveColor
        )

        // Step 2
        StepIndicator(
            state = getStepState(1, currentOrdinal),
            activeColor = activeColor,
            inactiveColor = inactiveColor
        )

        // Line 2-3
        StepLine(
            active = currentOrdinal >= 2,
            activeColor = activeColor,
            inactiveColor = inactiveColor
        )

        // Step 3
        StepIndicator(
            state = getStepState(2, currentOrdinal),
            activeColor = activeColor,
            inactiveColor = inactiveColor
        )
    }
}

private enum class StepState {
    PASSED, CURRENT, FUTURE
}

private fun getStepState(stepIndex: Int, currentOrdinal: Int): StepState {
    return when {
        stepIndex < currentOrdinal -> StepState.PASSED
        stepIndex == currentOrdinal -> StepState.CURRENT
        else -> StepState.FUTURE
    }
}

@Composable
private fun StepIndicator(
    state: StepState,
    activeColor: Color,
    inactiveColor: Color
) {
    val size = if (state == StepState.CURRENT) 20.dp else 12.dp
    val borderColor = if (state == StepState.FUTURE) inactiveColor else activeColor
    val borderWidth = if (state == StepState.CURRENT) 2.dp else 1.5.dp

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Outer ring
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = CircleShape
                )
        )

        // Inner circle for Current state
        if (state == StepState.CURRENT) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(activeColor, CircleShape)
            )
        }
    }
}

@Composable
private fun RowScope.StepLine(active: Boolean, activeColor: Color, inactiveColor: Color) {
    Spacer(
        modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .background(if (active) activeColor else inactiveColor)
    )
}