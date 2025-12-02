public class Goal {
    private int goalId;
    private int userId;
    private String goalName;
    private String description;

    private String createdAt; //store TIMESTAMP

    public Goal(int goalId, int userId, String goalName, String description, String createdAt) {
        this.goalId = goalId;
        this.userId = userId;
        this.goalName = goalName;
        this.description = description;
        this.createdAt = createdAt;
    }
//    getters and setter

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
