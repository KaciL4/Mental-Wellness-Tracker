# Mental Wellness Tracker 🧠

The Mental Wellness Tracker is a desktop application using Java (Java Swing) and SQLite to keep track of the user's well-being.
This application will help users better understand their mental health condition through a brief questionnaire that assesses their mood and their overall well-being.
***
### 📌 Prerequisites
1. **Java JDK 8**
2. **SQLite JDBC Driver:**  You must download the official Xerial SQLite JDBC driver JAR file.
    Download the latest version of sqlite-jdbc-{version}.jar from the Maven repository or a reliable source.
   (Here is where we download ours https://github.com/xerial/sqlite-jdbc/releases)
3. **Java IDE:** We use **IntelliJ**
4. **Optional:** Download DB Browser SQLite to check the database structure of this application

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
    │   ├── DailyEmotion.java
    │   ├── Goal.java
    │   ├── HabitLog.java
    │   ├── Mood.java
    │   ├── MoodOption.java
    |   ├── EmotionOpetion.java
    │   ├── User.java
    │   │
    │   ├── SQLConnector.java
    │   │
    │   ├── Localizable.java (interface)
    │   ├── GUI.java
    │   ├── AccountManagerPanel.java
    │   ├── ChangePasswordPanel.java
    │   ├── EmotionSelectionPanel.java
    │   ├── MainPanel.java
    │   ├── SighInPanel.java
    │   ├── SighUpPanel.java
    |   ├── EmotionSelectionPanel.java
    |   ├── GoalPanel.java
    |   └── HabitLogPanel.java
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
### ✅ User Guide 
1) **Sign in Panel**<br>
   - Enter your username and password
   - Click the Login button to log in to your account
   - If you don't have an account yet, click the Sign Up link to create your user account
3) **Sign up Panel**<br>
    - Enter a new username and a new password
    - Click the Create Account button to create your account
    - Click the Cancel button if you don't want to create a new account (It will bring you back to the Sign in Panel)
4) **Mood Panel**<br>
    - After logging in, you will be directly asked to select your current mood.
    - Select a mood and click the Submit Mood button to submit your current mood.
5) **Main Panel**<br>
    - You will see an overall mood score based on your entries from the Mood Panel
    - At the bottom, you have 3 buttons that will lead you to the EmotionSelection, Goal, and Habit log Panels, respectively.
    - On the top-right of the panel, you will find the Account Management Link

6) **Account Management Panel**<br>
   - It will display your current username and password
   - The Logout button is to log out of your account
   - The Delete this Account is to delete your current account from the system.
   - If you want to modify your password, click the Change Password Button. It will bring you to the Change Password Panel, where you have to enter your current password and your new password. Then click the Save Changes button to update your password to the new one.
7) **Emotion Selection Panel**<br>
    - Click the Add Emotion button in the Main Panel
    - It displays a list of emotions
    - Select all emotions that you are currently feeling. (It will save those checked emotions in the emotion table (DB table))
8) **Goal Panel**<br>
    - This panel keeps track of all the goals you want to achieve
    - Displays a table made up of 3 columns: Goal ID (auto-generated), Name, and Description
    - On the bottom of the panel, you will find input boxes for the name and description of the goal
    - Alongside the input boxes, there are 2 buttons to add and delete goals.
    - To edit a goal, simply select it from the table and double-click it. A pop-up window will appear where you can edit the name and description.
9) **Habit Log Panel**<br>
    - This panel keeps a log of what you've done to achieve your goals, aiming to build a habit.
    - Displays a table made up of 4 columns: Log ID (auto-generated), Goal, Notes, and Date
    - On the bottom of the panel, you will find a dropdown box to select a goal, then an input box labelled "Notes" to describe what you've done.
    - Alongside the input boxes, there are 2 buttons to add and delete logs.
    - To edit a log, simply select it from the table and double-click it. A pop-up window will appear where you can edit the name and description.
---
### 🔑 Database Schema 

The database consists of 8 tables: Users, Goals, Habit Logs, Mood Options, Daily Mood, Emotion Options, Daily Emotions, and Journals.
These tables (excluding 'Options' tables) keep unique logs for every different user.

1. **Users:** <br>
    This table will keep track of all users logged in to the app via username and password.
2. **Goals and Habits Logs:** <br>
    The user will be asked to set long-term goals when using the app. The goals table will help create a list of goals, with an ID, name, 
    description, and a target for how often they want to achieve this goal (e.g. daily, weekly, monthly, etc.). It will also save the creation date.
    To help reach these goals, the user will set one or more habits to do for each goal, along with the option to keep notes. The habit logs table will keep track of the completed date.
3. **Daily Mood and Mood Options:** <br>
    Every time the user opens the app, they will be asked about their overall mood that day. This will be stored in the Daily Mood table. 
    The user describes their mood through a scale created in the Mood Options table.
4. **Daily Emotions and Emotion Options:** <br>
    Similar to the previously mentioned tables, Daily Emotions will keep track of the choices (given by the Emotion Options table) the user made.
    The Daily Emotions table will ask the user to log any emotions they felt throughout the day, to have an even better understanding of their headspace.


   

