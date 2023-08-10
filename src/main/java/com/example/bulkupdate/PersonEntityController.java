package com.example.bulkupdate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/entity")
@RequiredArgsConstructor
@Slf4j
public class PersonEntityController {

    private final PersonEntityRepository repository;

    @GetMapping("")
    public ResponseEntity<List<Person>> getAllPerson() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Person person = repository.findById(id);
        if (person == null) {
            throw new ResourceNotFoundException("Person with id: [" + id + "] not found");
        }
        return ResponseEntity.ok(person);
    }

    @PostMapping("")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person req) {
        return new ResponseEntity<>(repository.save(req), HttpStatus.CREATED);
    }

    @PostMapping("/save-all")
    public ResponseEntity<List<Person>> createAllPerson(@Valid @RequestBody List<Person> personList) {
        long start = System.currentTimeMillis();
        List<Person> resultList = repository.saveAll(personList);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        log.info("[save-all] Time taken to create all records: " + String.valueOf(timeElapsed));
        return new ResponseEntity<>(resultList, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @Valid @RequestBody Person req) {
        Person person = repository.findById(id);
        if (person == null)
            throw new ResourceNotFoundException("Person with id: [" + id + "] not found");

        person.setFirstName(req.getFirstName());
        person.setLastName(req.getLastName());
        person.setGender(req.getGender());
        person.setEmail(req.getEmail());

        return ResponseEntity.ok(repository.update(person));
    }

    @PutMapping("/update-all")
    public ResponseEntity<List<Person>> updateAllPerson(@Valid @RequestBody List<Person> req) {
        List<Person> personList = new ArrayList<>();

        req.forEach(p -> {
            Person person = repository.findById(p.getId());
            if (person == null)
                throw new ResourceNotFoundException("Person with id: [" + p.getId() + "] not found");

            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setEmail(p.getEmail());
            person.setGender(p.getGender());
            personList.add(person);
        });

        long start = System.currentTimeMillis();
        List<Person> resultList = repository.updateAll(personList);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        log.info("[update-all] Time taken to update all records: " + String.valueOf(timeElapsed));

        return ResponseEntity.ok(resultList);
    }
}
