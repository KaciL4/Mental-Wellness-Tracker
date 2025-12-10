import java.util.Date;

public class HabitLog {
    private int logId;
    private int goalId;
    private int userId;
    private Date completedDate;
    private String note;

    public HabitLog(int logId, int goalId, int userId, Date completedDate, String note) {
        this.logId = logId;
        this.goalId = goalId;
        this.userId = userId;
        this.completedDate = completedDate;
        this.note = note;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

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

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getNotes() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
