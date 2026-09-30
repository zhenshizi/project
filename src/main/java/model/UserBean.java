package model;

import java.io.Serializable;

public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private byte[] userIcon;
    private String userName;
    private String mailAddress;
    private String password;
    private String secretQuestion;
    private String secretAnswer;
    private String nickname;
    private String groupId;
    private boolean isShare;

    public UserBean() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(byte[] userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isShare() {
        return isShare;
    }

    public boolean getIsShare() {
        return isShare;
    }

    public void setShare(boolean isShare) {
        this.isShare = isShare;
    }
}
