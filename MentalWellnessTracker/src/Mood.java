public class Mood {
    private  int moodId;
    private int userId;
    private String moodDate;//now
    private int moodOptionId;
    private String createdAt; //store TIMESTAMP

    public Mood(int moodId, int userId, String moodDate, int moodOptionId, String createdAt) {
        this.moodId = moodId;
        this.userId = userId;
        this.moodDate = moodDate;
        this.moodOptionId = moodOptionId;
        this.createdAt = createdAt;
    }

    public int getMoodId() {
        return moodId;
    }

    public void setMoodId(int moodId) {
        this.moodId = moodId;
    }

    public String getMoodDate() {
        return moodDate;
    }

    public void setMoodDate(String moodDate) {
        this.moodDate = moodDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMoodOptionId() {
        return moodOptionId;
    }

    public void setMoodOptionId(int moodOptionId) {
        this.moodOptionId = moodOptionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
