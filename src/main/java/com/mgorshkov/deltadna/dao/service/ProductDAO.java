package com.mgorshkov.deltadna.dao.service;

import com.mgorshkov.deltadna.dao.SessionUtil;
import com.mgorshkov.deltadna.dao.model.Machine;
import com.mgorshkov.deltadna.dao.model.Product;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * ProductDAO defines the CRUD operations which are valid for Products.
 */
public class ProductDAO {
    public void addProduct(Product bean){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        addProduct(session,bean);
        tx.commit();
        session.close();

    }

    private void addProduct(Session session, Product bean){
        Product product = new Product();
        product.setName(bean.getName());

        session.save(product);
    }

    public List<Product> getProducts(){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Product");
        List<Product> products =  query.list();
        session.close();
        return products;
    }

    public int deleteProduct(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Product where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }

    public int updateProduct(int id, Product product){
        if(id <=0)
            return 0;
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Product set name = :name where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        query.setString("name",product.getName());
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
}
