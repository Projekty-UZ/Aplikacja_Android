package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel

@Composable
fun TipScreen(navController: NavController) {
    val localDatabse = LocalDatabaseViewModel.current
    val tips = localDatabse.allTips.observeAsState(emptyList()).value

    LazyColumn(
        modifier = Modifier.
        fillMaxSize().
        padding(0.dp,20.dp,0.dp,0.dp),
    ) {
        item{
            Text(text = "Tips", fontSize = 30.sp)
        }
        items(tips) { tip ->
            Text(text = tip.ingredient, fontSize = 25.sp)
            Text(text = tip.tip, fontSize = 18.sp)
        }
    }
}