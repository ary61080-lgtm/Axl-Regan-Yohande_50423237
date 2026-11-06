package com.mycompany.cobamvc;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Helper SessionFactory Hibernate ORM
 * Package: com.mycompany.cobamvc
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Inisialisasi SessionFactory Hibernate gagal: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
