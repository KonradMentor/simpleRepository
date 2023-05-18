package org.example.ui;

public class StdConsoleDisplay implements Display{
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
