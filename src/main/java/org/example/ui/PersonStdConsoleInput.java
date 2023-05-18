package org.example.ui;

import org.example.model.Person;

import java.util.Scanner;

public class PersonStdConsoleInput implements Input<Person>, AutoCloseable {
    private final Scanner scanner;
    private final Display display;

    public PersonStdConsoleInput(Display display) {
        scanner = new Scanner(System.in);
        this.display = display;
    }

    @Override
    public void close() {
        scanner.close();
    }

    @Override
    public Person getInputFromUser() {
        //the logic of taking input from the user
        return new Person(getNameFromUser(), getSurnameFromUser());
    }

    private String getNameFromUser() {
        display.displayMessage("Enter name: ");
        return scanner.nextLine();
    }

    private String getSurnameFromUser() {
        display.displayMessage("Enter surname: ");
        return scanner.nextLine();
    }
}

