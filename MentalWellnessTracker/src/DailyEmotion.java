public class DailyEmotion {
    private int id;
    private int userId;
    private int moodId;
    private int optionId;
    private String selectedAt; // store TIMESTAMP

    public DailyEmotion(int id, int userId, int moodId, int optionId, String selectedAt) {
        this.id = id;
        this.userId = userId;
        this.moodId = moodId;
        this.optionId = optionId;
        this.selectedAt = selectedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMoodId() {
        return moodId;
    }

    public void setMoodId(int moodId) {
        this.moodId = moodId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getSelectedAt() {
        return selectedAt;
    }

    public void setSelectedAt(String selectedAt) {
        this.selectedAt = selectedAt;
    }
}
