package com.hibernate.service;

import com.hibernate.model.Person;

import java.util.List;

public interface PersonService {
    List<Person> getPerson();

    void savePerson(Person person);

    void updatePerson(Person person);

    void deletePerson(Person person);
}
