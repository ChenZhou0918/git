package perfsonarserver;

/**
 * POJO class to store User Logs in Cassandra
 *
 */
public class UserLog {
    public UserLog(String userId, String link, String action) {
        this.userId = userId;
        this.link = link;
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private String userId;
    private String link;
    private String action;
}

