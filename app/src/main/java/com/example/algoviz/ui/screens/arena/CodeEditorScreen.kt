package com.example.algoviz.ui.screens.arena

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.algoviz.ui.theme.DeepNavy
import com.example.algoviz.ui.theme.MintAccent
import com.example.algoviz.ui.theme.OrangeAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen(
    problemId: String,
    onNavigateBack: () -> Unit,
    viewModel: CodeEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val languages = listOf(
        62 to "Java",
        71 to "Python 3",
        54 to "C++",
        63 to "JavaScript"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Code Editor", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    var expanded by remember { mutableStateOf(false) }
                    TextButton(onClick = { expanded = true }) {
                        Text(
                            text = languages.find { it.first == uiState.languageId }?.second ?: "Select Language",
                            color = MintAccent
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        languages.forEach { (id, name) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    viewModel.onLanguageChange(id)
                                    expanded = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.submitCode(problemId) },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintAccent, contentColor = DeepNavy),
                        enabled = !uiState.isSubmitting
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = DeepNavy)
                        } else {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Run & Submit", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DeepNavy)
        ) {
            // Code Editor Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = uiState.code,
                    onValueChange = { viewModel.onCodeChange(it) },
                    modifier = Modifier.fillMaxSize(),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(MintAccent),
                    decorationBox = { innerTextField ->
                        if (uiState.code.isEmpty()) {
                            Text(
                                "Write your code here...",
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }

            // Results Area
            if (uiState.result != null || uiState.error != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Execution Result",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MintAccent
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        if (uiState.error != null) {
                            Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                        } else {
                            val result = uiState.result!!
                            Text(
                                text = "Status: ${result.status.description}",
                                fontWeight = FontWeight.SemiBold,
                                color = if (result.status.id == 3) MintAccent else OrangeAccent
                            )
                            if (result.stdout != null) {
                                Text(text = "Output:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                                Text(text = result.stdout, fontFamily = FontFamily.Monospace)
                            }
                            if (result.stderr != null) {
                                Text(text = "Error Output:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                                Text(text = result.stderr, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.error)
                            }
                            if (result.compile_output != null) {
                                Text(text = "Compile Output:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                                Text(text = result.compile_output, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }
    }
}