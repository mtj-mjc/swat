
package ch.mrnjec.swat.business;

import java.util.List;

import ch.mrnjec.swat.entities.Student;

/**
 * Einfache Businesslogik für Verwaltung von Studenten.
 */
public interface Students {

    /**
     * Liefert einen Student.
     * @param id Id des Student.
     * @return Student.
     */
    Student getById(long id);

    /**
     * Sucht nach Studierenden.
     * @param lastName Nachname.
     * @return Liste von Student.
     */
    List<Student> findByLastName(String lastName);

    /**
     * Liefert alle Studierenden.
     * @return Liste von Student.
     */
    List<Student> getAll();

    /**
     * Erstellt einen (neuen) Student.
     * @param student Student.
     * @return Student.
     */
    Student create(Student student);

    /**
     * Speichert/aktualisiert einen Student.
     * @param id id.
     * @param student Student.
     * @return OK oder NOK.
     */
    boolean update(long id, Student student);

    /**
     * Löscht einen Student.
     * @param id Id des Studenten.
     * @return OK oder NOK.
     */
    boolean delete(long id);
}