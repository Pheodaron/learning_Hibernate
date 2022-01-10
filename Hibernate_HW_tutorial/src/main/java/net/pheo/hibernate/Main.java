package net.pheo.hibernate;

import net.pheo.hibernate.entity.Student;
import net.pheo.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
    public static void main(String []args) {

        Student student = new Student("Ramesh", "Fadatare", "rameshfadatare@javaguides.com");
        Student student1 = new Student("John", "Cena", "john@javaguides.com");

        //Создать два объекта student

        Transaction transaction = null; //обнулить Transaction
        try (Session session = HibernateUtil.getSessionFectory().openSession()) {
            transaction = session.beginTransaction();
            session.save(student);
            session.save(student1);
            transaction.commit();
        } catch ( Exception e ) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        try (Session session = HibernateUtil.getSessionFectory().openSession()) {
            List<Student> students = session.createQuery("From Student", Student.class).list();
            students.forEach(s-> System.out.println(s.getFirstName()));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
