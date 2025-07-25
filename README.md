# TaskMaster - Online Task Management App

A modern, feature-rich task management application built with **Jetpack Compose** and **Firebase Database** in **Kotlin**.

## ✨ Features

### 🔐 Authentication
- User registration and login with Firebase Authentication
- Secure password-based authentication
- Automatic session management
- Password reset functionality

### 📝 Task Management
- **Create Tasks**: Add tasks with title, description, priority, and due dates
- **Edit Tasks**: Modify existing tasks seamlessly
- **Delete Tasks**: Remove completed or unwanted tasks
- **Mark as Complete**: Toggle task completion status
- **Priority Levels**: Set tasks as Low, Medium, or High priority with color coding
- **Categories**: Organize tasks into custom categories
- **Due Dates**: Set optional due dates for tasks

### 🔍 Advanced Features
- **Search**: Find tasks quickly by title or description
- **Filter**: Filter tasks by category and completion status
- **Real-time Sync**: All data syncs across devices instantly via Firebase
- **Modern UI**: Beautiful Material Design 3 interface
- **Responsive Design**: Optimized for different screen sizes

## 🚀 Tech Stack

- **Frontend**: Jetpack Compose (100% Kotlin UI)
- **Backend**: Firebase Realtime Database
- **Authentication**: Firebase Authentication
- **Architecture**: MVVM with Repository Pattern
- **Navigation**: Jetpack Navigation Compose
- **State Management**: Kotlin StateFlow and Compose State
- **Date Handling**: Kotlinx DateTime
- **Coroutines**: For asynchronous operations

## 📱 App Architecture

```
app/
├── data/                    # Data models
│   ├── Task.kt             # Task data class
│   ├── TaskCategory.kt     # Category data class
│   └── User.kt             # User data class
├── repository/             # Data layer
│   ├── TaskRepository.kt   # Task operations
│   └── AuthRepository.kt   # Authentication operations
├── viewmodel/              # Business logic
│   ├── TaskViewModel.kt    # Task management logic
│   └── AuthViewModel.kt    # Authentication logic
├── ui/screens/             # UI layer
│   ├── auth/              # Authentication screens
│   │   ├── LoginScreen.kt
│   │   └── SignUpScreen.kt
│   └── tasks/             # Task management screens
│       ├── TaskListScreen.kt
│       └── AddTaskScreen.kt
├── navigation/             # Navigation logic
│   └── TaskAppNavigation.kt
└── MainActivity.kt         # Entry point
```

## 🛠️ Setup Instructions

### Prerequisites
1. **Android Studio**: Latest version with Kotlin support
2. **Firebase Project**: Create a new Firebase project
3. **Minimum SDK**: API level 27 (Android 8.1)

### Firebase Setup
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use existing one
3. Add an Android app to your project
4. Download `google-services.json` and place it in `app/` directory
5. Enable the following Firebase services:
   - **Authentication** (Email/Password provider)
   - **Realtime Database**

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Ensure `google-services.json` is in the `app/` directory
4. Sync the project with Gradle files
5. Run the app on an emulator or physical device

## 📋 Usage Guide

### Getting Started
1. **Sign Up**: Create a new account with email and password
2. **Login**: Sign in with your credentials
3. **Add Tasks**: Tap the floating action button to create your first task

### Managing Tasks
- **View Tasks**: All tasks are displayed in a clean list format
- **Search**: Use the search bar to find specific tasks
- **Filter**: Tap the filter icon to filter by category or hide completed tasks
- **Edit**: Tap on any task or use the menu to edit
- **Complete**: Tap the circle icon to mark tasks as complete
- **Delete**: Use the menu options to delete tasks

### Categories
- **Create Categories**: Add new categories when creating/editing tasks
- **Filter by Category**: Use filters to view tasks from specific categories
- **Organization**: Keep your tasks organized with meaningful categories

## 🔧 Firebase Database Structure

```json
{
  "tasks": {
    "taskId1": {
      "id": "taskId1",
      "title": "Sample Task",
      "description": "Task description",
      "isCompleted": false,
      "priority": "HIGH",
      "category": "Work",
      "dueDate": "2024-12-31",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00",
      "userId": "userId1"
    }
  },
  "categories": {
    "categoryId1": {
      "id": "categoryId1",
      "name": "Work",
      "color": 4294901760,
      "userId": "userId1"
    }
  },
  "users": {
    "userId1": {
      "id": "userId1",
      "email": "user@example.com",
      "displayName": "John Doe",
      "createdAt": "2024-01-01T10:00:00",
      "lastLoginAt": "2024-01-01T10:00:00"
    }
  }
}
```

## 🎨 UI Screenshots

The app features a modern Material Design 3 interface with:
- Clean and intuitive login/signup screens
- Beautiful task list with priority indicators
- Comprehensive task creation/editing forms
- Smooth animations and transitions
- Dark/Light theme support (system default)

## 🔒 Security Features

- **Firebase Authentication**: Secure user authentication
- **User Isolation**: Users can only access their own tasks
- **Data Validation**: Client and server-side validation
- **Secure Database Rules**: Firebase security rules prevent unauthorized access

## 🚀 Performance Features

- **Real-time Updates**: Instant synchronization across devices
- **Efficient Querying**: Optimized Firebase queries
- **Lazy Loading**: Efficient list rendering with Jetpack Compose
- **Coroutines**: Non-blocking asynchronous operations
- **State Management**: Efficient state handling with StateFlow

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

- **Firebase**: For providing excellent backend services
- **Jetpack Compose**: For modern Android UI development
- **Material Design**: For beautiful and consistent design guidelines
- **Kotlin Community**: For the amazing language and ecosystem

---

**Built with ❤️ using Jetpack Compose and Firebase**