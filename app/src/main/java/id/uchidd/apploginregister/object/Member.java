package id.uchidd.apploginregister.object;

import java.util.Date;

public class Member {
    String username;
    String fullname;
    Date sessionExpiredDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getSessionExpiredDate() {
        return sessionExpiredDate;
    }

    public void setSessionExpiredDate(Date sessionExpiredDate) {
        this.sessionExpiredDate = sessionExpiredDate;
    }
}
