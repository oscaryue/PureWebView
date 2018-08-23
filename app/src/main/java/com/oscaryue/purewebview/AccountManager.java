package com.oscaryue.purewebview;

/**
 * Created by hp on 2018/6/26.
 */

public class AccountManager {
    private static final AccountManager ourInstance = new AccountManager();

    public static AccountManager getInstance() {
        return ourInstance;
    }

    private AccountManager() {
    }

    public boolean isLoggedIn() {
        return false;
    }
}
