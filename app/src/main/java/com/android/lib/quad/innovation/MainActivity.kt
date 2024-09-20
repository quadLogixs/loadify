package com.android.lib.quad.innovation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.lib.quad.innovation.ui.theme.FourTechInnovationsTheme
import com.android.lib.quad.logixs.R
import com.quadlogixs.loadify.Loadify
import com.quadlogixs.loadify.LoadifyType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FourTechInnovationsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Sample(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Sample(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(30.dp))
        // Generic Image
        Text(
            text = "Loadify Quick Image Loader",
            style = MaterialTheme.typography.titleLarge

        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Resource Image"
        )
        Loadify(image = R.drawable.logo, modifier = Modifier.size(50.dp))
        Spacer(modifier = Modifier.height(15.dp))

        // Network Image
        Text(
            text = "Network Image",
        )
        Loadify(
            image = "https://quadlogixs.com/wp-content/uploads/2024/09/Logo-1.png?resize=150%2C150&ssl=1",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))

        // Lottie Animation
        Text(
            text = "Lottie",
        )
        Loadify(
            image = R.raw.lottie_loader,
            loadifyType = LoadifyType.Lottie,
            placeholder = com.quadlogixs.loadify.R.drawable.ic_loader,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))

        // Resource Icon or Network Icon
        Text(
            text = "Icon",
        )
        Loadify(
            image = R.drawable.logo,
            loadifyType = LoadifyType.Icon,
            modifier = Modifier.size(50.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FourTechInnovationsTheme {
        Sample()
    }
}