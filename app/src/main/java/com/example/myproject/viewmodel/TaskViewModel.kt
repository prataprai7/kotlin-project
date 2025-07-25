package com.example.myproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.Task
import com.example.myproject.data.TaskCategory
import com.example.myproject.data.TaskPriority
import com.example.myproject.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val categories: List<TaskCategory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterCategory: String? = null,
    val searchQuery: String = "",
    val showCompleted: Boolean = true
)

class TaskViewModel : ViewModel() {
    private val taskRepository = TaskRepository()
    
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun loadTasks(userId: String) {
        viewModelScope.launch {
            combine(
                taskRepository.getTasksByUser(userId),
                taskRepository.getCategoriesByUser(userId)
            ) { tasks, categories ->
                _uiState.value = _uiState.value.copy(
                    tasks = tasks,
                    categories = categories,
                    isLoading = false
                )
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            taskRepository.addTask(task)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            taskRepository.updateTask(task)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        updateTask(updatedTask)
    }

    fun addCategory(category: TaskCategory) {
        viewModelScope.launch {
            taskRepository.addCategory(category)
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            taskRepository.deleteCategory(categoryId)
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun setFilterCategory(categoryName: String?) {
        _uiState.value = _uiState.value.copy(filterCategory = categoryName)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleShowCompleted() {
        _uiState.value = _uiState.value.copy(showCompleted = !_uiState.value.showCompleted)
    }

    fun getFilteredTasks(): List<Task> {
        val state = _uiState.value
        return state.tasks.filter { task ->
            val matchesCategory = state.filterCategory?.let { task.category == it } ?: true
            val matchesSearch = if (state.searchQuery.isNotBlank()) {
                task.title.contains(state.searchQuery, ignoreCase = true) ||
                task.description.contains(state.searchQuery, ignoreCase = true)
            } else true
            val matchesCompletionFilter = if (state.showCompleted) true else !task.isCompleted
            
            matchesCategory && matchesSearch && matchesCompletionFilter
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}