package model.application;

import model.domain.UserBean;

public class LoginStatus {

    private final UserBean userBean;
    private final boolean success;
    private final String reason;

    public LoginStatus(UserBean userBean, boolean success, String reason) {
        this.userBean = userBean;
        this.success = success;
        this.reason = reason;
    }

    public static LoginStatus success(UserBean user) {
        return new LoginStatus(user,true,"");
    }

    public static LoginStatus failure(String reason) {
        return new LoginStatus(null,false,reason);
    }


    public UserBean getUserBean() {
        return userBean;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }
}
