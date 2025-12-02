public class MoodOption {
    private int moodOptionId;
    private int moodLevel; //check between 1 and 5
    private String moodLabel;
    private String emoji;

    public MoodOption(int moodOptionId, int moodLevel, String moodLabel, String emoji) {
        this.moodOptionId = moodOptionId;
        this.moodLevel = moodLevel;
        this.moodLabel = moodLabel;
        this.emoji = emoji;
    }
//    getters and setters

    public int getMoodLevel() {
        return moodLevel;
    }

    public void setMoodLevel(int moodLevel) {
        this.moodLevel = moodLevel;
    }

    public int getMoodOptionId() {
        return moodOptionId;
    }

    public void setMoodOptionId(int moodOptionId) {
        this.moodOptionId = moodOptionId;
    }

    public String getMoodLabel() {
        return moodLabel;
    }

    public void setMoodLabel(String moodLabel) {
        this.moodLabel = moodLabel;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
