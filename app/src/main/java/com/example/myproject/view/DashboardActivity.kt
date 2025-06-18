package com.example.myproject.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myproject.ui.theme.MyProjectTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold { innerPadding ->
                DashboardBody(innerPadding)
            }
        }
    }
}

@Composable
fun DashboardBody(innerPadding: PaddingValues) {

    val context = LocalContext.current
    val activity = context as Activity

//    val email: String? = activity.intent.getStringExtra("email") // ? this means nullabe
//    val password: String? = activity.intent.getStringExtra("password")

    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)

    val localEmail : String? = sharedPreferences.getString("email","")
    val localpassword : String? = sharedPreferences.getString("password","")

    Column  (modifier = Modifier.padding(innerPadding).fillMaxSize())
    {
        Text("Good Morning, $localEmail" ) // $ ko kaam varaible dine ho
    }
}


@Preview
@Composable
fun Dashboardprebody() {
    DashboardBody(innerPadding = PaddingValues(0.dp))
}