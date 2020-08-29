package com.hibernate.repository;

import com.hibernate.model.Person;
import com.hibernate.service.PersonService;
import com.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PersonRepository implements PersonService {
    private static SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;

    public PersonRepository(){
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public List<Person>getPerson(){
        session = sessionFactory.openSession();
        try{
            String hql = "From Person";

            return session.createQuery(hql).list();
        }
        finally {
            session.close();
        }
    }
    @Override
    public void savePerson(Person person){
        session = sessionFactory.openSession();

        try{
            transaction = session.beginTransaction();

            session.save(person);

            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            if(transaction != null){
                transaction.rollback();
            }
        }
        finally {
            session.close();
        }

    }
    @Override
    public void updatePerson(Person person){
        session = sessionFactory.openSession();

        try{
            transaction = session.beginTransaction();

            session.update(person);

            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            if(transaction != null){
                transaction.rollback();
            }
        }
        finally {
            session.close();
        }
    }

    @Override
    public void deletePerson(Person id){
        session = sessionFactory.openSession();

        try{
            transaction = session.beginTransaction();

            session.delete(id);

            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            if(transaction != null){
                transaction.rollback();
            }
        }
        finally {
            session.close();
        }
    }





}
