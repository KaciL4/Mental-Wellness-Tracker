import java.sql.Connection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.util.*;
public class SQLConnector {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String SQL_SETUP_FILE = "setup.sql";

    private Connection connect() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(DB_URL);
            return c;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
            return null;
        }
    }

    public synchronized void intializeDb() {
        Connection conn = null;
        try {
            conn = connect();
            if (conn == null) return; // connection failed

            // DDL execution(sql file)
            executeDDLCommands(conn);
            // insert the fixed default data(DML)
            insertMoodOptions(conn);
            insertEmotionOptions(conn);
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void executeDDLCommands(Connection conn) throws SQLException {
        try {
            String sql = Files.readString(Paths.get(SQL_SETUP_FILE));
            // Execute each SQL command
            String[] commands = sql.split(";");
            for (String command : commands) {
                command = command.trim();
                // Skip empty commands and comments
                if (!command.isEmpty() && !command.startsWith("--")) {
                    Statement stmt = conn.createStatement();
                    try {
                        stmt.execute(command);
                    } finally {
                        stmt.close();
                    }
                }
            }
            System.out.println("SQL schema executed successfully!");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read SQL file. Check file path/location.");
            e.printStackTrace();
            throw new RuntimeException("Database setup file could not be read.", e);
        }
    }

    //    insert default values into MOOD_OPTIONS table
    private void insertMoodOptions(Connection conn) {
        String countSQL = "SELECT COUNT(*)FROM MOOD_OPTIONS";
        String insertSQL = "INSERT INTO MOOD_OPTIONS (MOOD_LEVEL,MOOD_LABEL, EMOJI) VALUES (?, ?, ?)";
        Object[][] moodData = {
                {1, "terrible", "😥"},
                {2, "bad", "😞"},
                {3, "okay", "😐"},
                {4, "good", "🙂"},
                {5, "great", "😊"}
        };
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSQL)) {
            if (rs.next() && rs.getInt(1) == 0) {
                //check if table is empty
                try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                    for (Object[] row : moodData) {
                        ps.setInt(1, (Integer) row[0]);
                        ps.setString(2, (String) row[1]);
                        ps.setString(3, (String) row[2]);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    System.out.println("MOOD_OPTIONS initialized successfully.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing MOOD_OPTIONS: " + e.getMessage());
        }
    }

    //    insert default values into EMOTIONS_OPTIONS table
    private void insertEmotionOptions(Connection conn) {
        String countSQL = "SELECT COUNT(*)FROM EMOTIONS_OPTIONS";
        String insertSQL = "INSERT INTO EMOTIONS_OPTIONS (EMOTION_NAME, EMOJI) VALUES (?, ?)";
        Object[][] emotionData = {
                {"sad", "🙁"}, {"tired", "🥱"}, {"relaxed", "😌"}, {"happy", "😄"},
                {"anxious", "😥"}, {"excited", "🤩"}, {"overwhelmed", "😣"}, {"lost", "🤨"},
                {"mad", "😠"}, {"scared", "😰"}, {"productive", "🤓"}, {"embarrassed", "😳"},
                {"sick", "🤒"}, {"risky", "😏"}, {"annoyed", "😒"}, {"calm", "😐"},
                {"not sure", "😶"}
        };
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSQL)) {
            if (rs.next() && rs.getInt(1) == 0) {
                //check if table is empty
                try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                    for (Object[] row : emotionData) {
                        ps.setString(1, (String) row[0]);
                        ps.setString(2, (String) row[1]);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    System.out.println("EMOTION_OPTIONS initialized successfully.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing EMOTION_OPTIONS: " + e.getMessage());
        }
    }

    //    CRUD Operations for USERS
//    Create
    public boolean insertUser(User user) {
        String sql = "INSERT INTO USERS(USERNAME, PASSWORD) VALUES(?,?)";
        try (Connection c = connect();
             PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setUserId(rs.getInt(1));//set genarate ID back to user object
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Insert User Error: " + e.getMessage());
            return false;
        }
    }

    //    Read
    public User getUserByUsername(String username) {
        String sql = "SELECT USER_ID, USERNAME,PASSWORD FROM USERS WHERE USERNAME= ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    //map result set data to User object
                    int id = rs.getInt("USER_ID");
                    String uname = rs.getString("USERNAME");
                    String pass = rs.getString("PASSWORD");
                    return new User(id, uname, pass);
                }
            }
            return null; // user not found
        } catch (SQLException e) {
            System.err.println("Reading User Error: " + e.getMessage());
            return null;
        }
    }

    //    Update
    public boolean updateUserPassword(int userId, String newPass) {
        String sql = "UPDATE USERS SET PASSWORD = ? WHERE USER_ID=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPass);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Update User Error: " + e.getMessage());
            return false;
        }
    }

    //    Delete
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Delete User Error: " + e.getMessage());
            return false;
        }
    }
//-----------------------------------------------------------------------------------------------------
//    CRUD for GOAL
//    create
    public int insertGoal(Goal goal){
        String sql ="INSERT INTO GOALS(USER_ID,GOAL_NAME,DESCRIPTION)VALUES(?,?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, goal.getUserId());
                ps.setString(2, goal.getGoalName());
                ps.setString(3, goal.getDescription());

                int rows =ps.executeUpdate();
                if(rows>0){
                    try(ResultSet rs = ps.getGeneratedKeys()){
                        if(rs.next()){
                            return rs.getInt(1);//return generated GOAL_ID
                        }
                    }
                }
                return -1; //Indicate failure
        }catch (SQLException e) {
            System.err.println("Insert Goal Error: " + e.getMessage());
            return -1;
        }
    }

    //ADDED
    // read single goal
    public Goal getGoalById(int goalId, int userId) {
        String sql ="SELECT GOAL_ID,GOAL_NAME,DESCRIPTION,CREATED_AT FROM GOALS WHERE GOAL_ID = ? AND USER_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, goalId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Goal(
                            rs.getInt("GOAL_ID"),
                            userId,
                            rs.getString("GOAL_NAME"),
                            rs.getString("DESCRIPTION"),
                            rs.getString("CREATED_AT")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Read Goal Error: " + e.getMessage());
        }
        return null;
    }
//    read
    public List<Goal>getUserGoals(int userId){
        String sql ="SELECT GOAL_ID,GOAL_NAME,DESCRIPTION,CREATED_AT FROM GOALS WHERE USER_ID = ?";
        List<Goal>goals =new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,userId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    goals.add(new Goal(
                            rs.getInt("GOAL_ID"),
                            userId,
                            rs.getString("GOAL_NAME"),
                            rs.getString("DESCRIPTION"),
                            rs.getString("CREATED_AT")
                    ));
                }
            }
        }catch (SQLException e) {
            System.err.println("Read Goal Error: " + e.getMessage());
        }
        return goals;
    }
//    update
    public boolean updateGoal(Goal goal){
        String sql ="UPDATE GOALS SET GOAL_NAME = ?, DESCRIPTION = ? WHERE GOAL_ID = ? AND USER_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, goal.getGoalName());
            ps.setString(2, goal.getDescription());
            ps.setInt(3, goal.getGoalId());
            ps.setInt(4, goal.getUserId());

            int rows = ps.executeUpdate();
            return rows > 0;

        }catch (SQLException e) {
            System.err.println("Update Goal Error: " + e.getMessage());
            return false;
        }
    }
//    delete
    public boolean deleteGoal(int goalId){
        String sql ="DELETE FROM GOALS WHERE GOAL_ID =?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1,goalId);
                return ps.executeUpdate()>0;
        }catch (SQLException e) {
            System.err.println("Delete Goal Error: " + e.getMessage());
            return false;
        }
    }
//    CR for HabitLog
//    create
    public boolean insertHabitLog(HabitLog log){
        String sql ="INSERT INTO HABIT_LOGS(GOAL_ID,USER_ID,COMPLETED_DATE,NOTES) VALUES(?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, log.getGoalId());
            ps.setInt(2, log.getUserId());

            // Format as YYYY-MM-DD string
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            if (log.getCompletedDate() != null) {
                ps.setString(3, sdf.format(log.getCompletedDate()));
            } else {
                ps.setString(3, sdf.format(new java.util.Date()));
            }

            ps.setString(4, log.getNotes());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Insert HabitLog Error: " + e.getMessage());
            return false;
        }
    }

    //ADDED
    // read single habit log
    public HabitLog getHabitLogById(int logId, int userId) {
        String sql = "SELECT LOG_ID, GOAL_ID, COMPLETED_DATE, NOTES FROM HABIT_LOGS WHERE LOG_ID = ? AND USER_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, logId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date completedDate = null;
                    try {
                        String dateStr = rs.getString("COMPLETED_DATE");
                        if (dateStr != null && !dateStr.isEmpty()) {
                            completedDate = java.sql.Date.valueOf(dateStr);
                        }
                    } catch (Exception e) {
                        completedDate = new java.sql.Date(System.currentTimeMillis());
                    }

                    return new HabitLog(
                            rs.getInt("LOG_ID"),
                            rs.getInt("GOAL_ID"),
                            userId,
                            completedDate,
                            rs.getString("NOTES")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Read HabitLog Error: " + e.getMessage());
        }
        return null;
    }

    // update 
    public boolean updateHabitLog(HabitLog log){
        String sql = "UPDATE HABIT_LOGS SET COMPLETED_DATE = ?, NOTES = ? WHERE LOG_ID = ? AND USER_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            if (log.getCompletedDate() != null) {
                ps.setString(1, sdf.format(log.getCompletedDate()));
            } else {
                ps.setString(1, sdf.format(new java.util.Date()));
            }
            ps.setString(2, log.getNotes());
            ps.setInt(3, log.getLogId());
            ps.setInt(4, log.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update HabitLog Error: " + e.getMessage());
            return false;
        }
    }

    // delete 
    public boolean deleteHabitLog(int logId, int userId){
        String sql ="DELETE FROM HABIT_LOGS WHERE LOG_ID =? AND USER_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1,logId);
                ps.setInt(2,userId);
                return ps.executeUpdate()>0;
        }catch (SQLException e) {
            System.err.println("Delete HabitLog Error: " + e.getMessage());
            return false;
        }
    }

    // Read
    public List<HabitLog>getLogsForGoal(int goalId, int userId){
        String sql ="SELECT LOG_ID, COMPLETED_DATE, NOTES FROM HABIT_LOGS WHERE GOAL_ID = ? AND USER_ID = ?";
        List<HabitLog>habits = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, goalId);
            ps.setInt(2, userId);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Date completedDate = null;
                    try {
                        // Get as string first, then parse
                        String dateStr = rs.getString("COMPLETED_DATE");
                        if (dateStr != null && !dateStr.isEmpty()) {
                            // Parse the date string (format: YYYY-MM-DD)
                            completedDate = java.sql.Date.valueOf(dateStr);
                        } else {
                            completedDate = new java.sql.Date(System.currentTimeMillis());
                        }
                    } catch (Exception e) {
                        System.err.println("Warning: Could not parse date, using current date");
                        completedDate = new java.sql.Date(System.currentTimeMillis());
                    }

                    habits.add(new HabitLog(
                            rs.getInt("LOG_ID"),
                            goalId,
                            userId,
                            completedDate,
                            rs.getString("NOTES")
                    ));
                }
            }
        }catch (SQLException e) {
            System.err.println("Read HabitLog Error: " + e.getMessage());
            e.printStackTrace();
        }
        return habits;
    }
//    CR for Daily Mood and Mood Option
//    create
    public int insertDailyMood(Mood mood){
//        MOOD_DATE = DEFAULT TO GET CURRENT DATE
        String sql ="INSERT INTO DAILY_MOOD(USER_ID,MOOD_OPTION_ID)VALUES(?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,mood.getUserId());
            ps.setInt(2,mood.getMoodOptionId());

            int rows = ps.executeUpdate();
            if(rows>0){
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()){
                        return rs.getInt(1); // return generated MOOD_ID
                    }
                }
            }
            return -1;
        }catch (SQLException e) {
            System.err.println("Insert DailyMood Error: " + e.getMessage());
            return -1;
        }
    }

    //ADDED
    // update 
    public boolean updateDailyMood(int moodId, int newMoodOptionId) {
        String sql = "UPDATE DAILY_MOOD SET MOOD_OPTION_ID = ? WHERE MOOD_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newMoodOptionId);
            ps.setInt(2, moodId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update DailyMood Error: " + e.getMessage());
            return false;
        }
    }

    // delete 
    public boolean deleteDailyMood(int moodId, int userId) {
        String sql = "DELETE FROM DAILY_MOOD WHERE MOOD_ID = ? AND USER_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, moodId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete DailyMood Error: " + e.getMessage());
            return false;
        }
    }

//    read
    public Mood getDailyMood(int userId,String date){
        String sql ="SELECT MOOD_ID, MOOD_OPTION_ID,CREATED_AT FROM DAILY_MOOD WHERE USER_ID=? AND MOOD_DATE=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,userId);
            ps.setString(2,date);
            try(ResultSet rs =ps.executeQuery()){
                if(rs.next()){
                    return new Mood(
                        rs.getInt("MOOD_ID"),
                            userId,
                            date,
                            rs.getInt("MOOD_OPTION_ID"),
                            rs.getString("CREATED_AT")
                    );
                }
            }
        }catch (SQLException e) {
            System.err.println("Read DailyMood Error: " + e.getMessage());
        }
        return null;
    }
    public double[]getAverageMoodLevel(int userId){
        String sql = "SELECT AVG(mo.MOOD_LEVEL), COUNT(dm.MOOD_ID) " +
                "FROM DAILY_MOOD dm " +
                "JOIN MOOD_OPTIONS mo ON dm.MOOD_OPTION_ID = mo.MOOD_OPTION_ID " +
                "WHERE dm.USER_ID = ?";
        try(Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    // rs.getDouble(1) will be AVG(mo.MOOD_LEVEL)
                    // rs.getInt(2) will be COUNT(dm.MOOD_ID)
                    return new double[]{rs.getDouble(1), rs.getInt(2)};
                }
            }
        }catch (SQLException e) {
            System.err.println("Read AverageMoodLevel Error: " + e.getMessage());
        }
        return new double[]{0.0, 0};
    }
    //    Read
    public List<MoodOption>getAllMoodOptions(){
        String sql ="SELECT MOOD_OPTION_ID,MOOD_LEVEL,MOOD_LABEL,EMOJI FROM MOOD_OPTIONS ORDER BY MOOD_LEVEL ASC";
        List<MoodOption> options = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                options.add(new MoodOption(
                        rs.getInt("MOOD_OPTION_ID"),
                        rs.getInt("MOOD_LEVEL"),
                        rs.getString("MOOD_LABEL"),
                        rs.getString("EMOJI")
                ));
            }
        }catch (SQLException e) {
            System.err.println("Read MoodOptions Error: " + e.getMessage());
        }
        return options;
    }
//    CR for Daily Emotion and emotion options
//    create
    public boolean insertDailyEmotion(DailyEmotion emotion){
        String sql ="INSERT INTO DAILY_EMOTIONS (USER_ID,MOOD_ID, OPTION_ID) VALUES (?,?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, emotion.getUserId());
            ps.setInt(2,emotion.getMoodId());
            ps.setInt(3, emotion.getOptionId());

            int rows = ps.executeUpdate();
            if(rows>0){
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()){
                        int generatedId = rs.getInt(1);
                        emotion.setId(generatedId);
                    }
                }
                return true;
            }
            return false;
        }catch (SQLException e) {
            System.err.println("Insert DailyEmotion Error: " + e.getMessage());
            return false;
        }
    }

    //ADDED
    // delete
    public boolean deleteDailyEmotion(int id) {
        String sql = "DELETE FROM DAILY_EMOTIONS WHERE ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete DailyEmotion Error: " + e.getMessage());
            return false;
        }
    }

//    Read
    public List<EmotionOption> getSelectedEmotionsForMood(int moodId){
        List<EmotionOption> emotions = new ArrayList<>();
        // fixed table name: EMOTIONS_OPTIONS (was EMOTION_OPTIONS)
        String sql ="SELECT eo.OPTION_ID, eo.EMOTION_NAME, eo.EMOJI " +
                "FROM DAILY_EMOTIONS de " +
                "JOIN EMOTIONS_OPTIONS eo ON de.OPTION_ID = eo.OPTION_ID " +
                "WHERE de.MOOD_ID = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, moodId);

            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    EmotionOption option = new EmotionOption(
                            rs.getInt("OPTION_ID"),
                            rs.getString("EMOTION_NAME"),
                            rs.getString("EMOJI")
                    );
                    emotions.add(option);
                }
            }
        }catch (SQLException e) {
            System.err.println("Read SelectedEmotionsForMood Error: " + e.getMessage());
        }
        return emotions;
    }
//    read
    public List<EmotionOption>getAllEmotionOptions(){
        String sql ="SELECT OPTION_ID,EMOTION_NAME,EMOJI FROM EMOTIONS_OPTIONS ORDER BY EMOTION_NAME ASC";
        List<EmotionOption> options = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                options.add(new EmotionOption(
                       rs.getInt("OPTION_ID"),
                       rs.getString("EMOTION_NAME"),
                       rs.getString("EMOJI")
                ));
            }
        }catch (SQLException e) {
            System.err.println("Read EmotionOption Error: " + e.getMessage());
        }
        return options;
    }
//-----------------------------------------------------------------------------------------------------
}
