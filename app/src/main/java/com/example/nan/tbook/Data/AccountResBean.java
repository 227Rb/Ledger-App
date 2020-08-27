package com.example.nan.tbook.Data;

public class AccountResBean {

    public   int id;
    public   String title;
    public   int accountIcon;

    public AccountResBean(int id, String title, int accountIcon) {
        this.id = id;
        this.title = title;
        this.accountIcon = accountIcon;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getAccountIcon() {
        return accountIcon;
    }


}
