package com.example.bulkupdate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository repository;
    private final EntityManager entityManager;

    public List<Person> getAll() {
        return repository.findAll();
    }

    public Optional<Person> getPersonById(Long id) {
        return repository.findById(id);
    }

    public Person savePerson(Person person) {
        return repository.save(person);
    }

    public List<Person> saveAllPerson(List<Person> personList) {
        return repository.saveAll(personList);
    }

    public Person savePersonEntMan(Person person) {

        return repository.save(person);
    }

    public void delete(Person person) {
        repository.delete(person);
    }

    public void deleteAll(List<Person> personList) {
        repository.deleteAll(personList);
    }
}
