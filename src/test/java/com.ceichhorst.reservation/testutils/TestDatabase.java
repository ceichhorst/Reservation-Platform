package com.ceichhorst.reservation.testutils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.ceichhorst.reservation.util.HibernateUtil;

public class TestDatabase {

    public static void runSQL(String sqlFile) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session. beginTransaction();

            String sql = new BufferedReader(new InputStreamReader(
                    TestDatabase.class.getClassLoader().getResourceAsStream(sqlFile)))
                    .lines()
                    .collect(Collectors.joining("\n"));

            for (String stmt : sql.split(";")) {
                if (!stmt.trim().isEmpty()) {
                    session.createNativeQuery(stmt).executeUpdate();
                }
            }

            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to run SQL file: " + sqlFile, e);
        }
    }
}
