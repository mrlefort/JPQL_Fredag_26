/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facade;

import entity.Student;
import entity.Studypoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Steffen
 */
public class Facade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
    EntityManager em = emf.createEntityManager();

    int sum;
    String firstname = "";
    String lastname = "";
    private int score;
    private String description;
    private int maxValue;

    public void findAllStudents() {
        TypedQuery<Student> q1 = em.createNamedQuery("Student.findAll", Student.class);

        List<Student> results = q1.getResultList();

        for (Student result : results) {
            System.out.println(result.getFirstname() + " " + result.getLastname());
        }

    }

    public void findStudentsNamedJan() {
        TypedQuery<Student> q2 = em.createNamedQuery("Student.findByFirstname", Student.class);
        q2.setParameter("firstname", "jan");

        List<Student> results = q2.getResultList();

        for (Student result : results) {
            System.out.println("finds students named jan: " + result.getFirstname() + result.getLastname());
        }
    }

    public void findStudentsLastNameOlsen() {
        TypedQuery<Student> q3 = em.createNamedQuery("Student.findByLastname", Student.class);
        q3.setParameter("lastname", "olsen");

        List<Student> results = q3.getResultList();

        for (Student result : results) {
            System.out.println("finds students with last name Olsen: " + result.getFirstname() + " " + result.getLastname());
        }
    }

    public void findStudentTotalSpSumGivenId(int i) {

        sum = 0;

        Query q4 = em.createQuery("SELECT s FROM Studypoint s, Student st WHERE st = s.studentId and st.id = 1");

        //TypedQuery<Studypoint> q4 = em.createNamedQuery("Studypoint.findById", Studypoint.class);
        //q4.setParameter("id", i);
        List<Studypoint> results = q4.getResultList();

        for (Studypoint result : results) {
            sum += result.getScore();

        }
        System.out.println("Student with ID: " + i + " has a total score of " + sum);
    }

    public void findStudentTotalSpSumForAllStudents() {

        sum = 0;

        TypedQuery<Studypoint> q5 = em.createNamedQuery("Studypoint.findAll", Studypoint.class);

        List<Studypoint> results = q5.getResultList();

        for (Studypoint result : results) {
            sum += result.getScore();

        }
        System.out.println("Sum of all student SP score: " + sum);
    }

    public void findStudentWithMostSp() {

        HashMap<Student, Integer> h = new HashMap();

        TypedQuery<Studypoint> q6 = em.createNamedQuery("Studypoint.findAll", Studypoint.class);

        List<Studypoint> results = q6.getResultList();

        //putter hvert object samt value 0 i HashMap
        for (Studypoint result : results) {
            h.put(result.getStudentId(), 0);
        }

        //Sammenligner result studentId med keys i HashMap. Passer de (som de selvfølgelig gør) så tilføjer den dén eksakte SP score til dén key's value.
        for (Studypoint result : results) {
            for (Map.Entry<Student, Integer> entry : h.entrySet()) {

                Student key = entry.getKey();

                if (result.getStudentId() == key) {
                    int value = entry.getValue();
                    Integer previousValue = h.get(result.getStudentId());
                    h.put(key, previousValue + result.getScore());
                }
            }
        }

        //iterate through HashMap and find the largest value.
        Entry<Student, Integer> maxEntry = null;

        for (Entry<Student, Integer> entry : h.entrySet()) {

            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;

            }
        }
        System.out.println("Student ID with highest score: " + maxEntry);
    }

    public void findStudentWithLeastSp() {

        HashMap<Student, Integer> h = new HashMap();

        TypedQuery<Studypoint> q7 = em.createNamedQuery("Studypoint.findAll", Studypoint.class);

        List<Studypoint> results = q7.getResultList();

        //putter hvert object samt value 0 i HashMap
        for (Studypoint result : results) {
            h.put(result.getStudentId(), 0);
        }

        //Sammenligner result studentId med keys i HashMap. Passer de (som de selvfølgelig gør) så tilføjer den dén eksakte SP score til dén key's value.
        for (Studypoint result : results) {
            for (Map.Entry<Student, Integer> entry : h.entrySet()) {

                Student key = entry.getKey();

                if (result.getStudentId() == key) {
                    int value = entry.getValue();
                    Integer previousValue = h.get(result.getStudentId());
                    h.put(key, previousValue + result.getScore());
                }
            }
        }

        //iterate through HashMap and find the largest value.
        Entry<Student, Integer> min = null;
        for (Entry<Student, Integer> entry : h.entrySet()) {
            if (entry.getKey() != null) {
                if (min == null || min.getValue() > entry.getValue()) {
                    min = entry;
                }
            }
        }

        System.out.println("Student with the lowest score: " + min);

    }

    public void createNewStudent(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;

        try {
            Student s = new Student(firstname, lastname);
            em.getTransaction().begin();
            em.persist(s);
            em.getTransaction().commit();
        } catch (Exception e) {
        }
        System.out.println("New student has been added: " + firstname + " " + lastname);

    }

    public void addStudyPoint(String firstname, String lastname, String description, int maxValue, int score) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
        this.maxValue = maxValue;
        this.score = score;



        TypedQuery<Student> q1 = em.createNamedQuery("Student.findAll", Student.class);

        List<Student> results = q1.getResultList();

        for (Student result : results) {
            if (result.getFirstname().equals(firstname) && result.getLastname().equals(lastname)) {
                int studentId = result.getId();
                Studypoint sp = new Studypoint(description, maxValue, score, studentId);
                sp.setStudentId(result);
                System.out.println(sp);
                try {
                    em.getTransaction().begin();
                    em.persist(sp);
                    em.getTransaction().commit();
                } catch (Exception e) {
                }
            }
        }

    }

}
