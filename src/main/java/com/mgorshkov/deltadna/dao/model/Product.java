package com.mgorshkov.deltadna.dao.model;

import javax.persistence.*;

/**
 * Product defines individual products which may be sold across many vending machines.
 */
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

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
}
