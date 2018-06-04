package ru.devsand.transmitter.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "customers")
public class Customer implements Entity {

    @DatabaseField(generatedId = true, useGetSet = true)
    private long id;
    @DatabaseField(columnName = "first_name", canBeNull = false, width = 20, useGetSet = true)
    private String firstName;
    @DatabaseField(columnName = "last_name", canBeNull = false, width = 20, useGetSet = true)
    private String lastName;
    @DatabaseField(columnName = "phone_number", canBeNull = false, unique = true, width = 15, useGetSet = true)
    private String phoneNumber;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
