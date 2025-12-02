public class User {
    private int userId;
    private String username;
    private String password;

    public User(int userId, String username,String password ) {
        this.password = password;
        this.username = username;
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "User[" +
                "ID=" + userId +
                ", Username='" + username + '\'' +
                ", Password='" + password + '\'' +
                ']';
    }
}
