import java.util.*;
public class Controller {
    private final SQLConnector dbConnector;
    private User currUser;
    public Controller(){
        this.dbConnector = new SQLConnector();
        dbConnector.intializeDb();
    }

    public User getCurrentUser(){
        return currUser;
    }
//    User table
//    CRUD Handlers
//    CREATE
    public String registerUser(String username,String password){
        if(username.isEmpty()||password.isEmpty()){
            return "Username and Password cannot be empty.";
        }
        if(dbConnector.getUserByUsername(username)!=null){
            return" Error: Username already exists.";
        }
        User newUser = new User(0, username, password);
        if(dbConnector.insertUser(newUser)){
            this.currUser=newUser;
            return"User successfully registered and logged in!";
        }else{
            return"Error: User failed to register.";
        }
    }
//    READ (login)
    public String loginUser(String username,String password){
        if(username.isEmpty()||password.isEmpty()){
            return "Username and Password cannot be empty.";
        }
        User user=dbConnector.getUserByUsername(username);
        if(user==null){
            return"Error: User not found.";
        }
        if(user.getPassword().equals(password)){
            this.currUser = user;
            return"Logged in successfully as "+user.getUsername();
        }else{
            return "Error: Invalid password.";
        }
    }
    // UPDATE
    public String updatePassword(String oldPassword, String newPassword){
        if(currUser == null)return"Error: Not logged in.";
        if(!currUser.getPassword().equals(oldPassword)) return "Error: Incorrect old password.";
        if(dbConnector.updateUserPassword(currUser.getUserId(),newPassword)){
            currUser.setPassword(newPassword);
            return "Successfully changed the password!";
        }else{
            return "Error: Failed to change the password.";
        }
    }
//    DELETE
    public String deleteCurrentUser(){
        if(currUser == null)return"Error: Not logged in.";
        if(dbConnector.deleteUser(currUser.getUserId())){
            String deletedUsername = currUser.getUsername();
            this.currUser=null; // log out
            return "Successfully deleted user '"+deletedUsername+"' !";
        }else{
            return"Error: Failed to delete user.";
        }
    }
    public void logout(){
        this.currUser=null;
    }
//    for the Goal table
//    Create
    public String addGoal(String name, String description){
        if(currUser==null) return"Error: Must be logged in to add a goal.";
        if(name.isEmpty())return"Error: Goal name cannot be empty";

        Goal newGoal= new Goal(0, currUser.getUserId(), name,description,null);
        int goalId = dbConnector.insertGoal(newGoal);

        if(goalId>0){
            return "Goal '"+name+"' added successfully!";
        }else{
            return "Error: Failed to add goal.";
        }
    }
//    delete
    public String removeGoal(int goalId){
        if(currUser==null)return "Error: Must be logged in.";

        if(dbConnector.deleteGoal(goalId)){
            return"Goal and all logs deleted successfully!";
        }else{
            return "Error: Failed to delete goal.";
        }
    }
//    read
    public List<Goal>getAllGoals(){
        if(currUser ==null)return Collections.emptyList();
        return dbConnector.getUserGoals(currUser.getUserId());
    }

    // habits
    //create
    public String createHabitLog(int logId, int userID, int goalId, Date completed_date, String notes){
        if(currUser==null) return"Error: Must be logged in to add a habit.";
        if(notes.isEmpty())return"Error: Habit notes cannot be empty";

        HabitLog habitLog = new HabitLog(0, goalId,currUser.getUserId(), null, notes);

        if(dbConnector.insertHabitLog(habitLog)){
            return "Log '"+logId+"' added successfully!";
        }else{
            return "Error: Failed to log habit completion.";
        }
    }
//    Moods Options and Emotion Options
//    Retrieve all mood options
    public List<MoodOption>getAvailableMoodOption(){
        return dbConnector.getAllMoodOptions();
    }
//    retrieve all emotion options
    public List<EmotionOption>getAvailableEmotionOptions(){
        return dbConnector.getAllEmotionOptions();
    }

//    daily + emotion
//    insert mood and emotion
    public String logDailyMood(int moodOptionId, List<Integer>emotionOptionIds){
        if(currUser==null)return "Error: Must be logged in to track mood.";
//        Insert Daily Mood
        Mood newMood = new Mood(0, currUser.getUserId(),null,moodOptionId,null);
        int moodId = dbConnector.insertDailyMood(newMood);

        if(moodId<0){
            return "Error: You have already logged your general mood for today.";
        }
//        Insert Daily Emotion
        if(emotionOptionIds!=null && !emotionOptionIds.isEmpty()){
            int successCount=0;
            for(int emotionOptionId:emotionOptionIds){
                DailyEmotion emotionLog= new DailyEmotion(0,currUser.getUserId(),moodId, emotionOptionId,null);
                if(dbConnector.insertDailyEmotion(emotionLog)){
                    successCount++;
                }
            }
//         requires emotions
            return String.format("Mood added successfully! (%d emotions tracked).",successCount);
        }else {
//            emotionId = null
            return "Mood added successfully! (No specific emotions is tracked yet.)";
        }
    }
//    Retrieve mood and emotion logged for current day
    public Mood getTodayMood(){
        if(currUser == null) return null;
        Mood mood = dbConnector.getDailyMood(currUser.getUserId(),new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return mood;
    }
//    insert Emotion
    public String logDailyEmotion(List<Integer>emotionOptionIds){
        if(currUser==null) return "Error: Must be logged in.";
        Mood todayMood = getTodayMood();
        if(todayMood==null)return "Error: Cannot track emotions. Please log your mood first.";

        int moodId = todayMood.getMoodId();
        if(emotionOptionIds!= null && !emotionOptionIds.isEmpty()){
            int successCount=0;
            for(int emotionOptionId:emotionOptionIds){
                DailyEmotion emotion = new DailyEmotion(0,currUser.getUserId(),moodId,emotionOptionId,null);
                if(dbConnector.insertDailyEmotion(emotion)){
                    successCount++;
                }
            }
            return String.format("Emotion tracked successfully! (%d emotions tracked).",successCount);
        }else{
            return "No specific emotion is selected.";
        }
    }
//   Method to calculate the average mood level
    public String calculateAverageMoodLevel(){
        if(currUser==null)return "Error: Not Logged in";
        double[]results= dbConnector.getAverageMoodLevel(currUser.getUserId());
        double avgMoodLevel=results[0];
        int moodCount= (int)results[1];
        if(moodCount==0){
            return "Mo mood data submitted yet.";
        }
        String averageMessage;
        String level;
        if(avgMoodLevel>=4.5){
            level="Excellent";
        } else if (avgMoodLevel>=3.5) {
            level="Good";
        } else if (avgMoodLevel>=2.5) {
            level="Okay";
        } else if (avgMoodLevel>=1.5) {
            level="Low";
        }else{
            level="Very Low";
        }
        averageMessage=String.format(
                "Based on %d entries, you overall average mood level is %.2f,which mean that you are "+level+".",moodCount,avgMoodLevel);
        return averageMessage;
    }
}


