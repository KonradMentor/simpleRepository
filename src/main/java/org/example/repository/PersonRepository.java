package org.example.repository;

import org.example.model.Person;

import java.util.List;

public interface PersonRepository extends AutoCloseable {
    void save(String name, String surname);
    void removeAll(String name, String surname);
    void removeById(int id);
    List<Person> findAll();
}
