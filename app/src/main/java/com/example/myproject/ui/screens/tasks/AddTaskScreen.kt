package com.example.myproject.ui.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myproject.data.Task
import com.example.myproject.data.TaskCategory
import com.example.myproject.data.TaskPriority
import com.example.myproject.viewmodel.AuthViewModel
import com.example.myproject.viewmodel.TaskViewModel
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskId: String = "",
    onNavigateBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    taskViewModel: TaskViewModel = viewModel()
) {
    val isEditing = taskId.isNotEmpty()
    
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var selectedCategory by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<String?>(null) }
    var showPriorityDropdown by remember { mutableStateOf(false) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    val authState by authViewModel.uiState.collectAsState()
    val taskState by taskViewModel.uiState.collectAsState()

    // Load existing task data if editing
    LaunchedEffect(taskId) {
        if (isEditing) {
            val existingTask = taskState.tasks.find { it.id == taskId }
            existingTask?.let { task ->
                title = task.title
                description = task.description
                selectedPriority = task.priority
                selectedCategory = task.category
                dueDate = task.dueDate
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(if (isEditing) "Edit Task" else "Add Task") 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (title.isNotBlank()) {
                                val currentUser = authState.currentUser
                                if (currentUser != null) {
                                    val task = if (isEditing) {
                                        val existingTask = taskState.tasks.find { it.id == taskId }
                                        existingTask?.copy(
                                            title = title,
                                            description = description,
                                            priority = selectedPriority,
                                            category = selectedCategory,
                                            dueDate = dueDate
                                        )
                                    } else {
                                        Task(
                                            title = title,
                                            description = description,
                                            priority = selectedPriority,
                                            category = selectedCategory,
                                            dueDate = dueDate,
                                            userId = currentUser.uid
                                        )
                                    }
                                    
                                    task?.let {
                                        if (isEditing) {
                                            taskViewModel.updateTask(it)
                                        } else {
                                            taskViewModel.addTask(it)
                                        }
                                        onNavigateBack()
                                    }
                                }
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text(
                            if (isEditing) "Update" else "Save",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (Optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5
            )

            // Priority Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Priority",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Box {
                        OutlinedButton(
                            onClick = { showPriorityDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(selectedPriority.color))
                                    )
                                    Text(
                                        text = selectedPriority.displayName,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Priority")
                            }
                        }

                        DropdownMenu(
                            expanded = showPriorityDropdown,
                            onDismissRequest = { showPriorityDropdown = false }
                        ) {
                            TaskPriority.values().forEach { priority ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(priority.color))
                                            )
                                            Text(
                                                text = priority.displayName,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedPriority = priority
                                        showPriorityDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Category Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        TextButton(
                            onClick = { showAddCategoryDialog = true }
                        ) {
                            Text("+ Add New")
                        }
                    }

                    Box(modifier = Modifier.padding(top = 12.dp)) {
                        OutlinedButton(
                            onClick = { showCategoryDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedCategory.ifEmpty { "Select Category" }
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Category")
                            }
                        }

                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("None") },
                                onClick = {
                                    selectedCategory = ""
                                    showCategoryDropdown = false
                                }
                            )
                            taskState.categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategory = category.name
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Due Date Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Due Date",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dueDate ?: "Select Date (Optional)"
                            )
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                        }
                    }

                    if (dueDate != null) {
                        TextButton(
                            onClick = { dueDate = null },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Clear Date")
                        }
                    }
                }
            }
        }
    }

    // Add Category Dialog
    if (showAddCategoryDialog) {
        AlertDialog(
            onDismissRequest = { 
                showAddCategoryDialog = false
                newCategoryName = ""
            },
            title = { Text("Add New Category") },
            text = {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newCategoryName.isNotBlank()) {
                            val currentUser = authState.currentUser
                            if (currentUser != null) {
                                val newCategory = TaskCategory(
                                    name = newCategoryName,
                                    userId = currentUser.uid
                                )
                                taskViewModel.addCategory(newCategory)
                                selectedCategory = newCategoryName
                            }
                            showAddCategoryDialog = false
                            newCategoryName = ""
                        }
                    },
                    enabled = newCategoryName.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showAddCategoryDialog = false
                        newCategoryName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                            dueDate = localDate.toString()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}