package com.example.acpia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.acpia.ui.DashboardScreen
import com.example.acpia.ui.theme.ACPIATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ACPIATheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                val viewModel = remember { com.example.acpia.viewmodel.InvestigationViewModel() }
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isLoggedIn) {
                        DashboardScreen(
                            viewModel = viewModel,
                            onLogout = { isLoggedIn = false }
                        )
                    } else {
                        LoginScreen(onLoginSuccess = { isLoggedIn = true })
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var key by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isMobile = configuration.screenWidthDp < 600

    val scrollState = androidx.compose.foundation.rememberScrollState()

    Row(modifier = Modifier.fillMaxSize()) {
        // Left Side: Login Form
        Column(
            modifier = Modifier
                .weight(if (isMobile) 1f else 1.05f)
                .fillMaxHeight()
                .background(Color.White.copy(alpha = 0.9f))
                .verticalScroll(scrollState)
                .padding(if (isMobile) 24.dp else 48.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.size(if (isMobile) 48.dp else 56.dp)
                ) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Kerala Cyber Police", fontWeight = FontWeight.ExtraBold, fontSize = if (isMobile) 18.sp else 20.sp)
                    Text("Secure Portal", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Text("Login to ACPIA", fontWeight = FontWeight.ExtraBold, fontSize = if (isMobile) 26.sp else 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Access evidence workflows, case intelligence, and sealed audit trails.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Officer email") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = key,
                onValueChange = { key = it },
                label = { Text("Access key") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp)
            )

            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (email.isNotBlank() && key.isNotBlank()) {
                        onLoginSuccess()
                    } else {
                        error = "Enter assigned credentials."
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in", fontWeight = FontWeight.Bold)
            }

            if (isMobile) {
                Spacer(modifier = Modifier.height(32.dp))
                RequirementsPanel(isMobile = true)
            }
        }

        // Right Side: Background/Requirements (Desktop only)
        if (!isMobile) {
            Box(
                modifier = Modifier
                    .weight(0.95f)
                    .fillMaxHeight()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                Color(0xFF24B8B0).copy(alpha = 0.08f)
                            )
                        )
                    )
                    .padding(48.dp),
                contentAlignment = Alignment.Center
            ) {
                RequirementsPanel(isMobile = false)
            }
        }
    }
}

@Composable
fun RequirementsPanel(isMobile: Boolean) {
    Surface(
        color = Color.White.copy(alpha = 0.95f),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = if (isMobile) 0.dp else 8.dp,
        border = if (isMobile) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
    ) {
        Column(modifier = Modifier.padding(if (isMobile) 16.dp else 24.dp)) {
            Text("Case Requirement Background", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "This investigation workflow is designed for lawful digital evidence review and secure evidence handling.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            listOf(
                "Preserve chain of custody.",
                "Protect victim identity.",
                "Generate audit trails.",
                "Cross-reference entities."
            ).forEach { req ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF25B07B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(req, fontSize = 12.sp)
                }
            }
        }
    }
}
