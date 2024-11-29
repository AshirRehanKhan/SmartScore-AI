package com.example.smartscore

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartscore.ui.theme.SmartscoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartscoreTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFFFF5E1)) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "progress_tracking") {
        composable("progress_tracking") { ProgressTrackingScreen(navController) }
        composable("risk_assessment") { RiskAssessmentScreen(navController) }
    }
}

@Composable
fun ExitDialog(showExitDialog: MutableState<Boolean>) {
    val context = LocalContext.current

    Dialog(onDismissRequest = { showExitDialog.value = false }) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Are you sure you want to exit?", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {
                            (context as? Activity)?.finish()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF800000))
                    ) {
                        Text("Yes", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { showExitDialog.value = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF800000))
                    ) {
                        Text("No", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressTrackingScreen(navController: NavController) {
    var showDropdown by remember { mutableStateOf(false) }
    var showExitDialog = remember { mutableStateOf(false) }

    val students = remember {
        mutableStateListOf(
            "Arlene McCoy" to 75,
            "Cody Fisher" to 85,
            "Esther Howard" to 90,
            "Ronald Richards" to 60,
            "Albert Flores" to 70,
            "Marvin McKinney" to 50,
            "Floyd Miles" to 40,
            "Courtney Henry" to 65,
            "Guy Hawkins" to 80,
            "Ralph Edwards" to 90,
            "Devon Lane" to 95,
            "Jenny Wilson" to 55
        )
    }
    var studentName by remember { mutableStateOf("") }
    var studentProgress by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartScore AI", fontSize = 20.sp) },
                navigationIcon = {
                    Box {
                        IconButton(onClick = { showDropdown = !showDropdown }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }
                        ) {
                            DropdownMenuItem(onClick = { navController.navigate("risk_assessment") }) {
                                Text("Risk Assessment")
                            }
                            DropdownMenuItem(onClick = { showExitDialog.value = true }) {
                                Text("Exit")
                            }
                        }
                    }
                },
                backgroundColor = Color(0xFF800000),
                contentColor = Color.White
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(Modifier.padding(16.dp)) {
                TextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Enter Student Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = studentProgress,
                    onValueChange = { studentProgress = it },
                    label = { Text("Enter Progress (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val progressValue = studentProgress.toIntOrNull()
                        if (studentName.isNotBlank() && progressValue != null && progressValue in 0..100) {
                            students.add(studentName to progressValue)
                            studentName = ""
                            studentProgress = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF800000))
                ) {
                    Text("Add New", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Progress Tracking",
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(students.size) { index ->
                        val (name, progress) = students[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name, fontSize = 16.sp, modifier = Modifier.weight(1f))
                            Canvas(
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(end = 8.dp)
                            ) {
                                drawArc(
                                    color = Color(0xFF800000),
                                    startAngle = -90f,
                                    sweepAngle = progress * 3.6f,
                                    useCenter = true
                                )
                            }
                            Text("$progress%", fontSize = 16.sp)
                        }
                        Divider(color = Color.Gray)
                    }
                }
            }
        }
    }

    if (showExitDialog.value) {
        ExitDialog(showExitDialog)
    }
}

@Composable
fun RiskAssessmentScreen(navController: NavController) {
    var showDropdown by remember { mutableStateOf(false) }
    var showExitDialog = remember { mutableStateOf(false) }

    val students = remember {
        mutableStateListOf(
            "Arlene McCoy" to 75,
            "Cody Fisher" to 85,
            "Esther Howard" to 90,
            "Ronald Richards" to 60,
            "Albert Flores" to 70,
            "Marvin McKinney" to 50,
            "Floyd Miles" to 40,
            "Courtney Henry" to 65,
            "Guy Hawkins" to 80,
            "Ralph Edwards" to 90,
            "Devon Lane" to 95,
            "Jenny Wilson" to 55
        )
    }
    var studentName by remember { mutableStateOf("") }
    var studentProgress by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Risk Assessment", fontSize = 20.sp) },
                navigationIcon = {
                    Box {
                        IconButton(onClick = { showDropdown = !showDropdown }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }
                        ) {
                            DropdownMenuItem(onClick = { navController.navigate("progress_tracking") }) {
                                Text("Progress Tracking")
                            }
                            DropdownMenuItem(onClick = { showExitDialog.value = true }) {
                                Text("Exit")
                            }
                        }
                    }
                },
                backgroundColor = Color(0xFF800000),
                contentColor = Color.White
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(Modifier.padding(16.dp)) {
                TextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Enter Student Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = studentProgress,
                    onValueChange = { studentProgress = it },
                    label = { Text("Enter Risk level") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val progressValue = studentProgress.toIntOrNull()
                        if (studentName.isNotBlank() && progressValue != null && progressValue in 0..100) {
                            students.add(studentName to progressValue)
                            studentName = ""
                            studentProgress = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF800000))
                ) {
                    Text("Add New", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Risk Assessment",
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(students.size) { index ->
                        val (name, progress) = students[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (progress in 70..100) {
                                Icon(
                                    Icons.Filled.Warning,
                                    contentDescription = "High Risk",
                                    tint = Color.Red,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                            Text(name, fontSize = 16.sp, modifier = Modifier.weight(1f))
                            Canvas(
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(end = 8.dp)
                            ) {
                                val color = when (progress) {
                                    in 0..40 -> Color.Green
                                    in 40..70 -> Color.Yellow
                                    else -> Color.Red
                                }
                                drawArc(
                                    color = color,
                                    startAngle = -90f,
                                    sweepAngle = progress * 3.6f,
                                    useCenter = true
                                )
                            }
                            Text("$progress%", fontSize = 16.sp)
                        }
                        Divider(color = Color.Gray)
                    }
                }
            }
        }
    }

    if (showExitDialog.value) {
        ExitDialog(showExitDialog)
    }
}