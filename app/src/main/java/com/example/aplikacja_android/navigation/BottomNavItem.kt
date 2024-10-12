package com.example.aplikacja_android.navigation

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavItem(
    val route:String,
    val name:String,
    val icon:Painter
)
