package org.example.repository;

import org.example.model.Person;

import java.util.LinkedList;
import java.util.List;

public class InMemoryPersonRepository implements PersonRepository {
    private final List<Person> persons;

    public InMemoryPersonRepository() {
        this.persons = new LinkedList<>();
    }

    @Override
    public void save(String name, String surname) {
        persons.add(new Person(name, surname));
        System.out.println("Saved: " + name + " " + surname);
    }

    @Override
    public void removeAll(String name, String surname) {
        persons.removeIf(person -> person.name().equals(name) && person.surname().equals(surname));
    }

    @Override
    public void removeById(int id) {
        persons.remove(id);
    }

    @Override
    public List<Person> findAll() {
        return persons;
    }

    @Override
    public void close() {
        System.out.println("Closing in memory repository");
    }
}
