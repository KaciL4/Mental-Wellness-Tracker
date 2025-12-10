import java.util.*;
public class Controller {
    private final SQLConnector dbConnector;
    private User currUser;

    private ResourceBundle messages;

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;
    }

    public Controller(){
        this.dbConnector = new SQLConnector();
        dbConnector.intializeDb();
    }

    public User getCurrentUser(){
        return currUser;
    }

    // Create
    public String registerUser(String username,String password){
        if(username.isEmpty()||password.isEmpty()){
            return messages.getString("err.emptyFields");
        }
        if(dbConnector.getUserByUsername(username)!=null){
            return messages.getString("err.userExists");
        }
        User newUser = new User(0, username, password);
        if(dbConnector.insertUser(newUser)){
            this.currUser=newUser;
            return messages.getString("msg.userReg");
        }else{
            return messages.getString("err.failed");
        }
    }

    // Read
    public String loginUser(String username,String password){
        if(username.isEmpty()||password.isEmpty()){
            return messages.getString("err.emptyFields");
        }
        User user=dbConnector.getUserByUsername(username);
        if(user==null){
            return messages.getString("err.userNotFound");
        }
        if(user.getPassword().equals(password)){
            this.currUser = user;
            return messages.getString("msg.userLogged")+ " " + user.getUsername();
        }else{
            return messages.getString("err.invalidPw");
        }
    }
    // Update
    public String updatePassword(String oldPassword, String newPassword){
        if(currUser == null) return messages.getString("err.login");
        if(!currUser.getPassword().equals(oldPassword)) return messages.getString("err.wrongPw");
        if(dbConnector.updateUserPassword(currUser.getUserId(),newPassword)){
            currUser.setPassword(newPassword);
            return messages.getString("msg.pwChanged");
        }else{
            return messages.getString("err.failed");
        }
    }

    // Delete
    public String deleteCurrentUser(){
        if(currUser == null) return messages.getString("err.login");
        if(dbConnector.deleteUser(currUser.getUserId())){
            String deletedUsername = currUser.getUsername();
            this.currUser=null; // log out
            return messages.getString("msg.accountDeleted")+ " " + deletedUsername;
        }else{
            return messages.getString("err.failed");
        }
    }
    public void logout(){
        this.currUser=null;
    }

    // Create
    public String addGoal(String name, String description){
        if(currUser==null) return messages.getString("err.login");
        if(name.isEmpty())return messages.getString("err.emptyFields");

        Goal newGoal= new Goal(0, currUser.getUserId(), name,description,null);
        int goalId = dbConnector.insertGoal(newGoal);

        if(goalId>0){
            return messages.getString("msg.add");
        }else{
            return messages.getString("err.failed");
        }
    }

    // Delete
    public String removeGoal(int goalId){
        if(currUser==null) return messages.getString("err.login");

        if(dbConnector.deleteGoal(goalId)){
            return messages.getString("msg.del");
        }else{
            return messages.getString("err.failed");
        }
    }

    // Read
    public List<Goal>getAllGoals(){
        if(currUser ==null)return Collections.emptyList();
        return dbConnector.getUserGoals(currUser.getUserId());
    }

//    // read a single goal
//    public Goal getGoal(int goalId){
//        if(currUser == null) return null;
//        return dbConnector.getGoalById(goalId, currUser.getUserId());
//    }

    // Update
    public String updateGoal(int goalId, String name, String description){
        if(currUser == null) return messages.getString("err.login");
        if(name == null || name.isEmpty()) return messages.getString("err.emptyFields");
        Goal g = new Goal(goalId, currUser.getUserId(), name, description, null);
        if(dbConnector.updateGoal(g)){
            return messages.getString("msg.update");
        } else {
            return messages.getString("err.failed");
        }
    }

    // HABITS

    // Create
    public String createHabitLog(int goalId, Date completedDate, String notes){
        if(currUser==null) return messages.getString("err.login");
        if(notes == null) notes = "";

        HabitLog habitLog = new HabitLog(0, goalId, currUser.getUserId(), completedDate, notes);

        if(dbConnector.insertHabitLog(habitLog)){
            return messages.getString("msg.add");
        }else{
            return messages.getString("err.failed");
        }
    }

    // Read
    public List<HabitLog> getLogsForGoal(int goalId){
        if(currUser == null) return Collections.emptyList();
        return dbConnector.getLogsForGoal(goalId, currUser.getUserId());
    }

    // Read single habit log
    public HabitLog getHabitLog(int logId){
        if(currUser == null) return null;
        return dbConnector.getHabitLogById(logId, currUser.getUserId());
    }

    // Update
    public String updateHabitLog(int logId, Date completedDate, String notes){
        if(currUser == null) return messages.getString("err.login");
        HabitLog existing = dbConnector.getHabitLogById(logId, currUser.getUserId());
        if(existing == null) return messages.getString("err.habitNotFound");
        HabitLog updated = new HabitLog(logId, existing.getGoalId(), currUser.getUserId(), completedDate, notes);
        if(dbConnector.updateHabitLog(updated)){
            return messages.getString("msg.update");
        } else {
            return messages.getString("err.failed");
        }
    }

    // Delete
    public String deleteHabitLog(int logId){
        if(currUser == null) return messages.getString("err.login");
        if(dbConnector.deleteHabitLog(logId, currUser.getUserId())){
            return messages.getString("msg.del");
        } else {
            return messages.getString("err.failed");
        }
    }

    // MOODS AND EMOTIONS

    // Retrieve all mood options
    public List<MoodOption>getAvailableMoodOption(){
        return dbConnector.getAllMoodOptions();
    }

    // Retrieve all emotion options
    public List<EmotionOption>getAvailableEmotionOptions(){
        return dbConnector.getAllEmotionOptions();
    }

    // daily + emotion *****************
    // Insert mood and emotion
    public String logDailyMood(int moodOptionId, List<Integer>emotionOptionIds){
        if(currUser==null)return messages.getString("err.login");

        // Insert Daily Mood
        Mood newMood = new Mood(0, currUser.getUserId(),null,moodOptionId,null);
        int moodId = dbConnector.insertDailyMood(newMood);

        // Insert Daily Emotion
        if(emotionOptionIds!=null && !emotionOptionIds.isEmpty()){
            int successCount=0;
            for(int emotionOptionId:emotionOptionIds){
                DailyEmotion emotionLog= new DailyEmotion(0,currUser.getUserId(),moodId, emotionOptionId,null);
                if(dbConnector.insertDailyEmotion(emotionLog)){
                    successCount++;
                }
            }
//         requires emotions
            return String.format(messages.getString("msg.moodAdd"),successCount);
        }else {
//            emotionId = null
            return messages.getString("msg.moodAddNo");
        }
    }

    // Retrieve mood and emotion logged for current day
    public Mood getTodayMood(){
        if(currUser == null) return null;
        Mood mood = dbConnector.getDailyMood(currUser.getUserId(),new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return mood;
    }

//    // update
//    public String updateTodayMood(int newMoodOptionId){
//        if(currUser == null) return messages.getString("err.login");
//        Mood today = getTodayMood();
//        if(dbConnector.updateDailyMood(today.getMoodId(), newMoodOptionId)){
//            return messages.getString("msg.update");
//        } else {
//            return messages.getString("err.failed");
//        }
//    }
//
//    // delete
//    public String removeTodayMood(){
//        if(currUser == null) return messages.getString("err.login");
//        Mood today = getTodayMood();
//        if(today == null) return "Error: No mood logged for today.";
//        if(dbConnector.deleteDailyMood(today.getMoodId(), currUser.getUserId())){
//            return "Today's mood removed successfully.";
//        } else {
//            return "Error: Failed to remove today's mood.";
//        }
//    }

    // Insert emotion
    public String logDailyEmotion(List<Integer>emotionOptionIds){
        if(currUser==null) return messages.getString("err.login");
        Mood todayMood = getTodayMood();
      //  if(todayMood==null)return "Error: Cannot track emotions. Please log your mood first.";

        int moodId = todayMood.getMoodId();
        if(emotionOptionIds!= null && !emotionOptionIds.isEmpty()){
            int successCount=0;
            for(int emotionOptionId:emotionOptionIds){
                DailyEmotion emotion = new DailyEmotion(0,currUser.getUserId(),moodId,emotionOptionId,null);
                if(dbConnector.insertDailyEmotion(emotion)){
                    successCount++;
                }
            }
            return String.format(messages.getString("msg.emotionTracked"),successCount);
        }else{
            return messages.getString("err.noEmotion");
        }
    }

    // read 
//    public List<EmotionOption> getSelectedEmotionsForToday(){
//        if(currUser == null) return Collections.emptyList();
//        Mood today = getTodayMood();
//        if(today == null) return Collections.emptyList();
//        return dbConnector.getSelectedEmotionsForMood(today.getMoodId());
//    }
//
//    // delete specific daily emotion
//    public String removeDailyEmotion(int dailyEmotionId){
//        if(currUser == null) return messages.getString("err.login");
//        if(dbConnector.deleteDailyEmotion(dailyEmotionId)){
//            return messages.getString("msg.del");
//        } else {
//            return messages.getString("err.failed");
//        }
//    }

//   Method to calculate the average mood level
    public String calculateAverageMoodLevel(){
        if(currUser==null)return messages.getString("err.login");
        double[]results= dbConnector.getAverageMoodLevel(currUser.getUserId());
        double avgMoodLevel=results[0];
        int moodCount= (int)results[1];
        if(moodCount==0){
            return messages.getString("err.noData");
        }
        String averageMessage;
        String level;
        if(avgMoodLevel>=4.5){
            level= messages.getString("mood.excellent");
        } else if (avgMoodLevel>=3.5) {
            level= messages.getString("mood.good");
        } else if (avgMoodLevel>=2.5) {
            level= messages.getString("mood.okay");
        } else if (avgMoodLevel>=1.5) {
            level= messages.getString("mood.low");
        }else{
            level= messages.getString("mood.vLow");
        }
        averageMessage=String.format(
                messages.getString("mood.msg")+ "  " + level + "." ,moodCount,avgMoodLevel);
        return averageMessage;
    }
}


