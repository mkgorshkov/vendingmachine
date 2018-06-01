package com.mgorshkov.deltadna.dao.model;

import javax.persistence.*;

/**
 * Machine defines an individual vending machine.
 */
@Entity
public class Machine {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private int insertedValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInsertedValue() {
        return insertedValue;
    }

    public void setInsertedValue(int insertedValue) {
        this.insertedValue = insertedValue;
    }
}
