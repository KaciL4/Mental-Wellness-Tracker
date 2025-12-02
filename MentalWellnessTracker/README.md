# Mental Wellness Tracker 🧠
***
## **Deliverable 1: Database Design and Core Connectivity**
The Mental Wellness is a desktop application using Java (Java swing) and SQLite to keep track of the user's well-being.
This application will help users to understand better their mental health condition through a brief questionnaire that assesses user's mood and  their overall well-being.
---
### 📌 Prerequisites
1. **Java JDK 8**
2. **SQLite JDBC Driver:**  You must download the official Xerial SQLite JDBC driver JAR file.
    Download the latest version of sqlite-jdbc-{version}.jar from the Maven repository or a reliable source.
   (Here where we download ours https://github.com/xerial/sqlite-jdbc/releases)
3. **Java IDE:** We use **IntelliJ**

---
### 📂 Project Structure
``` 
└── MentalWellnessTracker/
    ├── lib/
    │   └── sqlite-jdbc-3.51.0.0.jar
    │
    ├── resources/
    │   └── Resource Bundle/
    │      ├── MessageBundle.properties
    │      └── MessageBundle_fr.properties
    ├── src/
    │   ├── Main.java
    │   │
    │   ├── Controller.java
    │   │
    │   ├── DailyEmotion
    │   ├── Goal
    │   ├── HabitLog
    │   ├── Mood
    │   ├── MoodOption
    │   ├── User.java
    │   │
    │   ├── SQLConnector.java
    │   │
    │   ├── Localizable
    │   ├── GUI.java
    │   ├── AccountManagerPanel.java
    │   ├── ChangePasswordPanel.java
    │   ├── EmotionSelectionPanel.java
    │   ├── MainPanel.java
    │   ├── SighInPanel.java
    │   └── SighUpPanel.java
    │
    ├── database.db
    ├── MentalWellnessTracker.iml
    ├── README.md 
    └── setup.sql
```
---
### 📋 Step-by-Step Setup
1. Download the ZIP folder and extract it
2. In the zip folder, there will be a folder called MentalWellnessTracker
3. Open the folder in a Java IDE (recommend IntelliJ)
4. Run the Main.java class to launch the application
---
### ✅ Usage Guide to test CRUD Operation
1) **CREATE: 🛠️**<br>
    - Create a user in the Sign-up page
2) **READ: 📖**<br>
    - Login to the created user in the Sign-in page
    - If the user exists in the system, it will log in to the main page
3) **UPDATE: 📰**<br>
    - Change the created user's password in the Account Management page
    - By confirming the password change with the "Save Changes" button, it will update to the new password.
    - After changing the password, colse the application and reopen it to log with the new user password.
4) **DELETE: 🗑️**<br>
    - Delete the user by clicking the "Delete Account" button
    - It will ask user to confirm the deletion of its account
    - It will log user out of its account
    - If user tries to log in back, the system will decline the deleted user account
---
### 🔑 Database Schema 

The database consists of 8 tables: Users, Goals, Habit Logs, Mood Options, Daily Mood, Emotion Options, Daily Emotions, and Journals.
These tables (excluding 'Options' tables) keep unique logs for every different user.

1. **Users:** <br>
    This table will keep track of all users logged in the app via username and password.
2. **Goals and Habits Logs:** <br>
    The user will be asked to set long-term goals when using the app. The goals table will help create a list of goals, with an ID, name, 
    description, and a target for how often they want to achieve this goal (e.g. daily,weekly,monthly,etc.). It will also save the creation date.
    To help reach these goals, the user will set one or more habits to do for each goal along with the option to keep notes. The habit logs table will keep track of the completed date.
3. **Daily Mood and Mood Options:** <br>
    Every time the user opens the app, they will be asked about their overall mood that day, this will be stored in the Daily Mood table. 
    The user describes their mood through a scale created in the Mood Options table.
4. **Daily Emotions and Emotion Options:** <br>
    Similar to the previously mentioned tables, Daily Emotions will keep track of the choices (given by the Emotion Options table) the user made.
    The Daily Emotions table will ask the user to log any emotions they felt throughout the day, to have an even better understanding of their headspace.

   