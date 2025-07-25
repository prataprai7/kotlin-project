package com.example.myproject.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: String = "",
    val dueDate: String? = null,
    val createdAt: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
    val updatedAt: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
    val userId: String = ""
)

enum class TaskPriority(val displayName: String, val color: Long) {
    LOW("Low", 0xFF4CAF50),
    MEDIUM("Medium", 0xFFFF9800),
    HIGH("High", 0xFFF44336)
}

data class TaskCategory(
    val id: String = "",
    val name: String = "",
    val color: Long = 0xFF2196F3,
    val userId: String = ""
)