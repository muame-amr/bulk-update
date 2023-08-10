package com.example.bulkupdate;

/**
 * Projection for {@link Person}
 */
public interface PersonInfo {
    Long getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getGender();
}