package com.example.myproject.repository

import com.example.myproject.data.Task
import com.example.myproject.data.TaskCategory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TaskRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val tasksRef = database.child("tasks")
    private val categoriesRef = database.child("categories")

    // Tasks operations
    suspend fun addTask(task: Task): Result<String> {
        return try {
            val taskId = tasksRef.push().key ?: return Result.failure(Exception("Failed to generate task ID"))
            val taskWithId = task.copy(id = taskId, updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString())
            tasksRef.child(taskId).setValue(taskWithId).await()
            Result.success(taskId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            val updatedTask = task.copy(updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString())
            tasksRef.child(task.id).setValue(updatedTask).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            tasksRef.child(taskId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getTasksByUser(userId: String): Flow<List<Task>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasks = mutableListOf<Task>()
                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    if (task != null && task.userId == userId) {
                        tasks.add(task)
                    }
                }
                trySend(tasks.sortedByDescending { it.createdAt })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        tasksRef.addValueEventListener(listener)
        awaitClose { tasksRef.removeEventListener(listener) }
    }

    // Categories operations
    suspend fun addCategory(category: TaskCategory): Result<String> {
        return try {
            val categoryId = categoriesRef.push().key ?: return Result.failure(Exception("Failed to generate category ID"))
            val categoryWithId = category.copy(id = categoryId)
            categoriesRef.child(categoryId).setValue(categoryWithId).await()
            Result.success(categoryId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCategoriesByUser(userId: String): Flow<List<TaskCategory>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = mutableListOf<TaskCategory>()
                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(TaskCategory::class.java)
                    if (category != null && category.userId == userId) {
                        categories.add(category)
                    }
                }
                trySend(categories.sortedBy { it.name })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        categoriesRef.addValueEventListener(listener)
        awaitClose { categoriesRef.removeEventListener(listener) }
    }

    suspend fun deleteCategory(categoryId: String): Result<Unit> {
        return try {
            categoriesRef.child(categoryId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}