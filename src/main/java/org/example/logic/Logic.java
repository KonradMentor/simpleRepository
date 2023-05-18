package org.example.logic;

import org.example.model.Person;
import org.example.repository.PersonRepository;
import org.example.ui.Display;
import org.example.ui.Input;

public class Logic {
    private final PersonRepository repository;
    private final Input<Person> personInput;
    private final Display display;

    public Logic(PersonRepository repository, Input<Person> personInput, Display display) {
        this.repository = repository;
        this.personInput = personInput;
        this.display = display;
    }

    public void handlePersonSave() {
        Person providedPerson = personInput.getInputFromUser();
        repository.save(providedPerson.name(), providedPerson.surname());
        display.displayMessage("All persons: ");
        repository.findAll().forEach(person -> display.displayMessage(person.toString()));
    }
}
