package com.fridgekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.fridgekeeper.ui.screen.DigitalScreen
import com.fridgekeeper.ui.screen.FridgeScreen
import com.fridgekeeper.ui.screen.PetScreen
import com.fridgekeeper.ui.screen.StatsScreen
import com.fridgekeeper.ui.theme.Background
import com.fridgekeeper.ui.theme.FridgeKeeperTheme
import com.fridgekeeper.viewmodel.DigitalViewModel
import com.fridgekeeper.viewmodel.FridgeViewModel
import com.fridgekeeper.viewmodel.PetViewModel
import com.fridgekeeper.viewmodel.StatsViewModel
import com.fridgekeeper.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModelFactory: ViewModelFactory by lazy {
        val app = application as FridgeApp
        ViewModelFactory(
            app.repository,
            app.digitalRepository,
            app.petRepository,
            app.statsRepository
        )
    }

    private val fridgeViewModel: FridgeViewModel by viewModels { viewModelFactory }
    private val digitalViewModel: DigitalViewModel by viewModels { viewModelFactory }
    private val petViewModel: PetViewModel by viewModels { viewModelFactory }
    private val statsViewModel: StatsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FridgeKeeperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    MainScreen(
                        fridgeViewModel = fridgeViewModel,
                        digitalViewModel = digitalViewModel,
                        petViewModel = petViewModel,
                        statsViewModel = statsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    fridgeViewModel: FridgeViewModel,
    digitalViewModel: DigitalViewModel,
    petViewModel: PetViewModel,
    statsViewModel: StatsViewModel
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Kitchen, contentDescription = null) },
                    label = { Text("冰箱收纳") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.Devices, contentDescription = null) },
                    label = { Text("数码产品") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Pets, contentDescription = null) },
                    label = { Text("宠物用品") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Filled.Analytics, contentDescription = null) },
                    label = { Text("物品统计") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> FridgeScreen(viewModel = fridgeViewModel)
                1 -> DigitalScreen(viewModel = digitalViewModel)
                2 -> PetScreen(viewModel = petViewModel)
                3 -> StatsScreen(viewModel = statsViewModel)
            }
        }
    }
}
