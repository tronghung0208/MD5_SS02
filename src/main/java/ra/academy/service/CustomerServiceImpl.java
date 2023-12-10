package ra.academy.service;

import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import ra.academy.model.Customer;

import java.util.List;
@Service
public class CustomerServiceImpl implements ICustomerService{
    private static final SessionFactory sessionFactory;
    private static final EntityManager entityManager;
    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.conf.xml").buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    @SuppressWarnings("Turn off Lombok's warning")
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public List<Customer> findAll() {
        String queryStr = "select c from Customer as c";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        return query.getResultList();
    }

    @Override
    public void save(Customer customer) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            if (customer.getId() != null) {
                Customer customerInDatabase = findById(customer.getId());
                customerInDatabase.setName(customer.getName());
                customerInDatabase.setAddress(customer.getAddress());
                customerInDatabase.setEmail(customer.getEmail());
            }
            session.saveOrUpdate(customer);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.isActive();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }


    }

    @Override
    public Customer findById(Long id) {
        String queryStr = "select c from Customer as c where c.id = :id";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(findById(id));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
