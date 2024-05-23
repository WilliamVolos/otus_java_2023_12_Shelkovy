package ru.otus.appcontainer.exceptions;

public class IncorrectConfigApplication extends RuntimeException{
    public IncorrectConfigApplication(String message){
        super(message);
    }
}
