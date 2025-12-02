public class EmotionOption {
    private int optionId;
    private  String emotionName;
    private String emoji;

    public EmotionOption(int optionId, String emotionName, String emoji) {
        this.optionId = optionId;
        this.emotionName = emotionName;
        this.emoji = emoji;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getEmotionName() {
        return emotionName;
    }

    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
