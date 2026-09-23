# Notero

Notero is a simple and efficient note-taking application designed to help you organize your thoughts, ideas, and tasks. With a clean interface and powerful features, Notero makes it easy to capture and manage your notes.

![Notero Logo](images/notero_icon.png)

## Development
To set up the development environment for Notero, follow these steps:
1. Clone the repository
2. Open the project in your preferred IDE (e.g., Android Studio).
3. Make sure you have the necessary dependencies installed. You can do this by running:
   ```bash
   ./gradlew build
   ```

### Generate the APK
To generate the APK for Notero, follow these steps:
1. Open a terminal and navigate to the project directory.
2. Run the following command to build the APK:
   ```bash
   ./gradlew assembleDebug
   ```
3. The generated APK can be found in the `app/build/outputs/apk/debug/` directory.
4. You can install the APK on your Android device by transferring it and opening it.

### Generate the Documentation
To generate the documentation for Notero, you can use a documentation generator like Javadoc or Dokka. Follow these steps:
1. Open a terminal and navigate to the project directory.
2. Run the following command to generate the documentation:
   ```bash
   ./gradlew dokkaGenerate
   ```
3. The generated documentation can be found in the `app/build/dokka/html` directory.
4. You can view the documentation by opening the `index.html` file in a web browser.

