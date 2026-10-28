package com.hazia.sisyphusfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hazia.sisyphusfit.ui.AppShell
import com.hazia.sisyphusfit.ui.theme.stoneEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stoneEdgeToEdge()
        setContent { AppShell() }
    }
}
