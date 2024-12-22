package com.example.finalhomework.util_classes;

public class User {
    private String accountString; // 用户账号
    private String passwordString; // 用户密码
    private String nickNameString; // 用户昵称
    private int isAdmin;

    // 构造方法
    public User() {
    }

    public User(String accountString, String passwordString, String nickNameString) {
        this.accountString = accountString;
        this.passwordString = passwordString;
        this.nickNameString = nickNameString;
    }

    // Getter 和 Setter 方法
    public String getAccountString() {
        return accountString;
    }

    public void setAccountString(String accountString) {
        this.accountString = accountString;
    }

    public String getPasswordString() {
        return passwordString;
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    public String getNickNameString() {
        return nickNameString;
    }

    public void setNickNameString(String nickNameString) {
        this.nickNameString = nickNameString;
    }


    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    // 重写 toString 方法便于打印对象信息
    @Override
    public String toString() {
        return "User{" +
                "accountString='" + accountString + '\'' +
                ", passwordString='" + passwordString + '\'' +
                ", nickNameString='" + nickNameString + '\'' +
                '}';
    }

    public boolean isAdmin() {
        return isAdmin == 1;
    }
}