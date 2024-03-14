package ru.otus.exceptions;

public class InvalidRequestedMoney extends Exception{
    public InvalidRequestedMoney(String message){
        super(message);
    }
}
