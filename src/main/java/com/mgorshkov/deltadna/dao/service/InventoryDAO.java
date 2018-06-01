package com.mgorshkov.deltadna.dao.service;

import com.mgorshkov.deltadna.dao.SessionUtil;
import com.mgorshkov.deltadna.dao.model.Inventory;
import com.mgorshkov.deltadna.dao.model.Register;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * InventoryDAO defines the CRUD operations which are valid for Inventory.
 */
public class InventoryDAO {
    public void addInventory(Inventory bean){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        addInventory(session,bean);
        tx.commit();
        session.close();
    }

    private void addInventory(Session session, Inventory bean){
        Inventory inventory = new Inventory();
        inventory.setProduct(bean.getProduct());
        inventory.setCurrentNumber(bean.getCurrentNumber());
        inventory.setMaxNumber(bean.getMaxNumber());
        inventory.setPrice(bean.getPrice());
        inventory.setMachine(bean.getMachine());

        session.save(inventory);
    }

    public List<Inventory> getInventory(){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Inventory");
        List<Inventory> registers =  query.list();
        session.close();
        return registers;
    }

    public List<Inventory> getInventoryByMachineId(int machineId){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Inventory where machineid = :machineid");
        query.setInteger("machineid", machineId);
        List<Inventory> registers =  query.list();
        session.close();
        return registers;
    }

    public Inventory getInventoryByMachineIdAndProduct(int machineId, int productId){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Inventory where machineid = :machineid and productid = :productid");
        query.setInteger("machineid", machineId);
        query.setInteger("productid", productId);
        Inventory inventory = null;

        if(query.list().size() > 0){
           inventory = (Inventory) query.list().get(0);
        }
        session.close();
        return inventory;
    }


    public int deleteInventory(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Inventory where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }

    public boolean replenishInventory(Inventory inventory){
        if(inventory.getId() <= 0){
            return false;
        }

        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Inventory set currentNumber = :maxNumber where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",inventory.getId());
        query.setInteger("maxNumber",inventory.getMaxNumber());
        query.executeUpdate();
        tx.commit();
        session.close();
        return true;
    }

    public void sellItemFromInventory(Inventory inventory){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Inventory set currentNumber = currentNumber - 1 where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",inventory.getId());
        query.executeUpdate();
        tx.commit();
        session.close();
    }

}
