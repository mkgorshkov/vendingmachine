package com.mgorshkov.deltadna.dao;

/**
 * Enum which describes the valid coins vending machines accept.
 */
public enum Coins{
    NICKEL(5), DIME(10), QUARTER(25), DOLLAR(100);

    private int centsValue;

    Coins(int cents){
        this.centsValue = cents;
    }

    public int getValue(){
        return centsValue;
    }

}
