package com.mgorshkov.deltadna.dao.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mgorshkov.deltadna.dao.Coins;
import com.mgorshkov.deltadna.dao.SessionUtil;
import com.mgorshkov.deltadna.dao.model.Machine;
import com.mgorshkov.deltadna.dao.model.Product;
import com.mgorshkov.deltadna.dao.model.Register;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

/**
 * RegisterDAO defines the CRUD operations which are valid for Registers.
 */
public class RegisterDAO {
    public void addRegister(Register bean){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        addRegister(session,bean);
        tx.commit();
        session.close();

    }

    private void addRegister(Session session, Register bean){
        Register register = new Register();
        register.setCurrencyName(bean.getCurrencyName());
        register.setCurrencyValue(bean.getCurrencyValue());
        register.setCurrentNumber(bean.getCurrentNumber());
        register.setMaxNumber(bean.getMaxNumber());

        session.save(register);
    }

    public List<Register> getRegisters(){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Register");
        List<Register> registers =  query.list();
        session.close();
        return registers;
    }

    public List<Register> getRegistersByMachineId(int machineId){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Register where machineId = :id");
        query.setInteger("id",machineId);

        List<Register> registers =  query.list();
        session.close();
        return registers;
    }

    public int deleteRegister(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Register where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }

    public boolean replenishChange(Register register){
        if(register.getId() <= 0){
            return false;
        }

        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Register set currentNumber = :maxNumber where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",register.getId());
        query.setInteger("maxNumber",register.getMaxNumber());
        query.executeUpdate();
        tx.commit();
        session.close();
        return true;
    }

    public void removeChange(int machineId, Coins coin, int numCoinsStart, int numCoinsUsed){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Register set currentNumber = :newCurrentNumber where machineId = :machineId and currencyValue = :currencyValue";
        Query query = session.createQuery(hql);
        query.setInteger("newCurrentNumber", numCoinsStart - numCoinsUsed);
        query.setInteger("currencyValue",coin.getValue());
        query.setInteger("machineId",machineId);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    public void addChange(int machineId, Coins coin){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Register set currentNumber = currentNumber+1 where machineId = :machineId and currencyValue = :currencyValue";
        Query query = session.createQuery(hql);
        query.setInteger("currencyValue",coin.getValue());
        query.setInteger("machineId",machineId);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    public String dispenseChange(Machine machine){
        List<Register> registerList = getRegistersByMachineId(machine.getId());

        HashMap<Integer, Integer> currentNumCoins = new HashMap<>();

        for(Register r : registerList){
            currentNumCoins.put(r.getCurrencyValue(), r.getCurrentNumber());
        }

        String returnString = "";

        Gson gson = new Gson();
        JsonObject reponseObject = new JsonObject();

        Session session = SessionUtil.getSession();
        session.refresh(machine);

        /*
            If some coins aren't available we go to the next biggest coin which can give the user change.
         */
        int change = (int)(Math.ceil(machine.getInsertedValue()));
        int dollars = Math.round((int)change/100);
        if(currentNumCoins.get(Coins.DOLLAR.getValue()) >= dollars){
            removeChange(machine.getId(), Coins.DOLLAR, currentNumCoins.get(Coins.DOLLAR.getValue()), dollars);
            returnString += dollars +" dollars. ";
            change=change%100;
        }
        int quarters = Math.round((int)change/25);
        if(currentNumCoins.get(Coins.QUARTER.getValue()) >= quarters){
            removeChange(machine.getId(), Coins.QUARTER, currentNumCoins.get(Coins.QUARTER.getValue()), quarters);
            returnString += quarters +" quarters. ";
            change=change%25;
        }
        int dimes = Math.round((int)change/10);
        if(currentNumCoins.get(Coins.DIME.getValue()) >= dimes){
            removeChange(machine.getId(), Coins.DIME, currentNumCoins.get(Coins.DIME.getValue()), dimes);
            returnString += dimes +" dimes. ";
            change=change%10;
        }
        int nickels = Math.round((int)change/5);
        if(currentNumCoins.get(Coins.NICKEL.getValue()) >= nickels){
            removeChange(machine.getId(), Coins.NICKEL, currentNumCoins.get(Coins.NICKEL.getValue()), nickels);
            returnString += nickels +" nickels. ";
            change=change%5;
        }

        return returnString;
    }
}
