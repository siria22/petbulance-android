package com.example.presentation.screen.feature.search.views.result

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMs
import com.example.presentation.component.ui.molecule.FilterBottomSheet
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXXS
import com.example.presentation.screen.feature.search.HospitalSearchData
import com.example.presentation.screen.feature.search.UserLocationData
import com.example.presentation.screen.feature.search.views.common.RowChipFilters
import com.example.presentation.screen.feature.search.views.search.HospitalSearchQueryUiModel
import com.example.presentation.screen.feature.search.views.search.SearchBar

@Composable
fun ResultView() {

}