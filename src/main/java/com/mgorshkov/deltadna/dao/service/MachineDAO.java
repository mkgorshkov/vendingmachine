package com.mgorshkov.deltadna.dao.service;

import com.mgorshkov.deltadna.dao.Coins;
import com.mgorshkov.deltadna.dao.SessionUtil;
import com.mgorshkov.deltadna.dao.model.Machine;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * MachineDAO defines the CRUD operations which are valid for Machine.
 */
public class MachineDAO {
    public void addMachine(Machine bean){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        addMachine(session,bean);
        tx.commit();
        session.close();

    }

    private void addMachine(Session session, Machine bean){
        Machine machine = new Machine();
        machine.setName(bean.getName());

        session.save(machine);
    }

    public List<Machine> getMachines(){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Machine");
        List<Machine> machines =  query.list();
        session.close();
        return machines;
    }

    public Machine getMachineByID(int id){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Machine where id = :id");
        query.setInteger("id",id);
        Machine machine = null;

        if(query.list().size() > 0){
            machine = (Machine) query.list().get(0);
        }
        session.close();
        return machine;
    }

    public int deleteMachine(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Machine where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }

    public int updateMachine(int id, Machine machine){
        if(id <=0)
            return 0;
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Machine set name = :name where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        query.setString("name",machine.getName());
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }

    public void flushInsertedCoins(int machineId){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Machine set insertedValue = 0 where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",machineId);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    public void insertCoins(int machineId, Coins coin){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Machine set insertedValue = insertedValue + :coinValue where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",machineId);
        query.setInteger("coinValue",coin.getValue());
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    public void sellItem(int machineId, int price){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Machine set insertedValue = insertedValue - :productValue where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",machineId);
        query.setInteger("productValue",price);
        query.executeUpdate();
        tx.commit();
        session.close();
    }
}
