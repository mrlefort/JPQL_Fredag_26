/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import Facade.Facade;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Steffen
 */
public class Tester {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
    EntityManager em = emf.createEntityManager();
    
    public static void main(String[] args) {
        Facade f = new Facade();
        
        f.findAllStudents();
        f.findStudentsNamedJan();
        f.findStudentsLastNameOlsen();
        f.findStudentTotalSpSumGivenId(1);
        f.findStudentTotalSpSumForAllStudents();
        f.findStudentWithMostSp();
        f.findStudentWithLeastSp();
        f.createNewStudent("Steffen", "Lefort"); //kører man den flere gange så husk at udkommentere dette eller skifte navn. Ellers kommer der flere med samme navn.
        f.addStudyPoint("Steffen", "Lefort", "JPQL_FredagsOPG", 5, 5);
    }
}
