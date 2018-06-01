package com.mgorshkov.deltadna.dao.model;

import javax.persistence.*;

/**
 * A register represents a cash register or more accurately a roll of a single type of coins. Each register has
 * a currencyName ex. Nickel, the value of the currency it accepts as well as the maximum number of coins which can be placed
 * in this register. The number of coins must be between 0 and max in order to vend properly. If there is a coin type missing,
 * change will be given with the next largest type. Ex. if there are no more $1 coins, users will receive 4 $0.25 as change. Restocking
 * the coins will set currentNumber to maxNumber.
 */
@Entity
public class Register {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column
    private String currencyName;

    @Column
    private int currencyValue;

    @Column
    private int currentNumber;

    @Column
    private int maxNumber;

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

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public int getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(int currencyValue) {
        this.currencyValue = currencyValue;
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
}
