package com.example.bulkupdate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/person")
@RequiredArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService service;

    @GetMapping("")
    public ResponseEntity<List<Person>> getAllPerson() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return service.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id: [" + id + "] not found"));
    }

    @PostMapping("")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person req) {
        return new ResponseEntity<>(service.savePerson(req), HttpStatus.CREATED);
    }

    @PostMapping("/save-all")
    public ResponseEntity<List<Person>> createAllPerson(@Valid @RequestBody List<Person> personList) {
        long start = System.currentTimeMillis();
        List<Person> resultList = service.saveAllPerson(personList);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        log.info("[save-all] Time taken to create all records: " + String.valueOf(timeElapsed));
        return new ResponseEntity<>(resultList, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @Valid @RequestBody Person req) {
        return service.getPersonById(id).map(person -> {
            person.setFirstName(req.getFirstName());
            person.setLastName(req.getLastName());
            person.setEmail(req.getEmail());
            person.setGender(req.getGender());
            return ResponseEntity.ok(service.savePerson(person));
        }).orElseThrow(() -> new ResourceNotFoundException("Person with id: [" + id + "] not found"));
    }

    @PutMapping("/update-all")
    public ResponseEntity<List<Person>> updateAllPerson(@Valid @RequestBody List<Person> req) {
        List<Person> personList = new ArrayList<>();

        req.forEach(p -> {
            Optional<Person> personOptional = service.getPersonById(p.getId());

            if (personOptional.isEmpty())
                throw new ResourceNotFoundException("Person with id: [" + p.getId() + "] not found");

            Person person = personOptional.get();
            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setEmail(p.getEmail());
            person.setGender(p.getGender());
            personList.add(person);
        });

        long start = System.currentTimeMillis();
        List<Person> resultList = service.saveAllPerson(personList);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        log.info("[update-all] Time taken to update all records: " + String.valueOf(timeElapsed));

        return ResponseEntity.ok(resultList);
    }

    @DeleteMapping("")
    public ResponseEntity<List<Person>> deletePersonById(@PathVariable Long id) {
        Optional<Person> personOptional = service.getPersonById(id);
        if (personOptional.isEmpty())
            throw new ResourceNotFoundException("Person with id: [" + id + "] not found");
        Person person = personOptional.get();
        service.delete(person);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<List<Person>> deleteAllPerson(@Valid @RequestBody List<Person> personList) {
        List<Person> deletedList = new ArrayList<>();
        personList.forEach(p -> {
            Optional<Person> personOptional = service.getPersonById(p.getId());
            if (personOptional.isEmpty())
                throw new ResourceNotFoundException("Person with id: [" + p.getId() + "] not found");
            Person person = personOptional.get();
            deletedList.add(person);
        });
        service.deleteAll(deletedList);
        return ResponseEntity.noContent().build();
    }
}
