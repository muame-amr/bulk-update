package com.example.bulkupdate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonEntityRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final int BATCH_SIZE = 50;

    @Transactional
    public Person save(Person person) {
        if (person == null)
            throw new IllegalArgumentException("Person cannot be null");
        try {
            entityManager.persist(person);
            return person;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save person: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<Person> saveAll(List<Person> persons) {
        if (persons == null || persons.isEmpty())
            throw new IllegalArgumentException("Person lists cannot be null or empty");
        try {
            for (int i = 0; i < persons.size(); ++i) {
                if (i > 0 && i % BATCH_SIZE == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
                Person person = persons.get(i);
                entityManager.persist(person);
            }
            return persons;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save person: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Person update(Person person) {
        if (person == null)
            throw new IllegalArgumentException("Person cannot be null");
        try {
            return entityManager.merge(person);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save person: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<Person> updateAll(List<Person> persons) {
        if (persons == null || persons.isEmpty())
            throw new IllegalArgumentException("Person lists cannot be null or empty");

        List<Person> updatedLists = new ArrayList<>();
        try {
            for (int i = 0; i < persons.size(); ++i) {
                if (i > 0 && i % BATCH_SIZE == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
                Person person = persons.get(i);
                updatedLists.add(entityManager.merge(person));
            }
            return updatedLists;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save person: " + e.getMessage(), e);
        }
    }

    public List<Person> findAll() {
        String jpql = "SELECT p from Person p";
        TypedQuery<Person> query = entityManager.createQuery(jpql, Person.class);
        return query.getResultList();
    }

    public Person findById(Long id) {
        return entityManager.find(Person.class, id);
    }
}
