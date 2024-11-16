package com.aurelien.study_tracker.exception;

public class AccountNotVerifiedException extends RuntimeException{
    public AccountNotVerifiedException(){
        super("Account not verified. Please verify your account.");
    }
}
