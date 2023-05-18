package org.example;

import org.example.logic.Logic;
import org.example.repository.InMemoryPersonRepository;
import org.example.repository.SqlitePersonRepository;
import org.example.ui.Display;
import org.example.ui.PersonStdConsoleInput;
import org.example.ui.StdConsoleDisplay;

/**
 * Hello world!
 */
public class ClientsCode {

    private static final String DB_PATH = "/Users/konradlekawski/Databases/MyPersonalDB.db";

    public static void main(String[] args) {
        //PersonInput and PersonRepository have AutoCloseable interface
        // so we can use try-with-resources
        // and we don't have to close them manually
        Display display = new StdConsoleDisplay();
        try (var personInput = new PersonStdConsoleInput(display); //Display is injected
             var userRepository = new InMemoryPersonRepository()) {
            Logic logic = new Logic(userRepository, personInput, display); // The same display instance is injected here
            logic.handlePersonSave();
        }
        //No need to catch exceptions
    }
}
