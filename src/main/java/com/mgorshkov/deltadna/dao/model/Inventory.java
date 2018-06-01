package com.mgorshkov.deltadna.dao.model;

import javax.persistence.*;

/**
 * Inventory takes care of the relationship between the product and the machine. Here the price is set on an individual
 * machine basis. The number of items must be between 0 and maxNumber in order to be purchaseable.
 *
 * When inventory gets restocked for any item, the numItems goes to maxNumber (ex. filling up as many spots as possible).
 *
 */
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "productId",
            referencedColumnName="id")
    private Product product;

    @Column
    private int currentNumber;

    @Column
    private int maxNumber;

    @Column
    private int price;

    @ManyToOne
    @JoinColumn(name = "machineid",
            referencedColumnName="id")
    private Machine machine;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }
}
