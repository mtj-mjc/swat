
package ch.mrnjec.swat.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import ch.mrnjec.swat.entities.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fake Implementation für Verwaltung von Studenten. Speichert die Daten nur in einer Map (in
 * Memory).
 */
@Singleton
public class StudentsMemory implements Students {

    private static final Logger LOG = LoggerFactory.getLogger(StudentsMemory.class);
    private static int idInc = 0;

    private Map<Long, Student> students;

    /**
     * Erzeugt eine statische Liste mit drei Studenten.
     */
    public StudentsMemory() {
        if (this.students == null) {
            this.students = new HashMap<>();
            this.students.put(1L, new Student(1, "Herbert", "Grone", 3));
            this.students.put(2L, new Student(2, "Babette", "Zweifel", 4));
            this.students.put(3L, new Student(3, "Markus", "Kaiser", 11));
            idInc = this.students.size();
            LOG.debug("Es wurden {} Studenten wurden erzeugt.", idInc);
        }
    }

    /**
     * @see Students#getById(long)
     */
    @Override
    public Student getById(final long id) {
        LOG.info("Student mit id={} gelesen.", id);
        return this.students.get(id);
    }

    /**
     * @see Students#findByLastName(java.lang.String)
     */
    @Override
    public List<Student> findByLastName(final String lastName) {
        List<Student> list = this.students.values().stream().filter(s -> s.getLastName().contains(lastName))
                    .collect(Collectors.toList());
        LOG.info("Liste mit {} Studenten mit Nachname '{}' gefunden.", list.size(), lastName);
        return list;
    }

    /**
     * @see Students#getAll()
     */
    @Override
    public List<Student> getAll() {
        List<Student> list = this.students.values().stream().collect(Collectors.toList());
        LOG.info("Liste mit {} Studenten gelesen.", students.size());
        return list;
    }

    /**
     * @see Students#create(Student)
     */
    @Override
    public Student create(final Student student) {
        student.setId(++idInc);
        this.students.put((long) student.getId(), student);
        LOG.info("Student '{}' erstellt.", student);
        return student;
    }

    /**
     * @see Students#update(long, Student)
     */
    @Override
    public boolean update(final long id, final Student student) {
        final boolean exists = this.students.get(id) != null;
        if (exists) {
            student.setId((int) id);
            this.students.put(id, student);
            LOG.info("Student {} aktualisiert.", student);
        } else {
            LOG.info("Student mit id={} nicht vorhanden.", id);
        }
        return exists;
    }

    /**
     * @see Students#remove(long)
     */
    @Override
    public boolean delete(final long id) {
        final Student student = students.remove(id);
        LOG.info("Student mit id={} geloescht.", student);
        return (student != null);
    }
}
