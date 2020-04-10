
package ch.mrnjec.swat.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.mrnjec.swat.entities.Student;

/**
 * Testcases für Student Service. Verwendet Micronaut.io.
 */
final class StudentsMemoryTest {

    private Students students;

    @BeforeEach
    void beforeEach() {
        this.students = new StudentsMemory();
    }

    /**
     * Testcase für {@link StudentsMemory#getAll()}.
     */
    @Test
    void testGetAll() {
        assertThat(new StudentsMemory().getAll()).hasSize(3);
    }

    /**
     * Testcase für {@link StudentsMemory#getById(long)}.
     */
    @Test
    void testGetFirst() {
        final Student student = this.students.getById(1L);
        assertThat(student).isNotNull();
        assertThat(student.getId()).isEqualTo(1);
        assertThat(student.getFirstName()).isEqualTo("Herbert");
    }

    /**
     * Testcase für {@link StudentsMemory#findByLastName(String)}.
     */
    @Test
    void testFindByNameNot() {
        assertThat(this.students.findByLastName("XXX")).hasSize(0);
    }

    /**
     * Testcase für {@link StudentsMemory#findByLastName(String)}.
     */
    @Test
    void testFindByName() {
        final List<Student> list = this.students.findByLastName("Zweifel");
        assertThat(list).hasSize(1);
    }

    /**
     * Testcase für {@link StudentsMemory#store(Student)}.
     */
    @Test
    void testCreate() {
        students.create(new Student("Vorname", "Nachname", 1));
        final Student student = students.getById(4L);
        assertThat(student.getFirstName()).isEqualTo("Vorname");
        assertThat(student.getLastName()).isEqualTo("Nachname");
    }

    /**
     * Testcase für {@link StudentsMemory#update(long, Student)}.
     */
    @Test
    void testUpdateExistingStudent() {
        final boolean updated = students.update(1L, new Student("Vorname", "Nachname", 1));
        assertTrue(updated);
        final Student student = students.getById(1L);
        assertAll("Student", () -> assertThat(student.getFirstName()).isEqualTo("Vorname"),
                    () -> assertThat(student.getLastName()).isEqualTo("Nachname"));
    }

    /**
     * Testcase für {@link StudentsMemory#update(long, Student)}.
     */
    @Test
    void testUpdateNotExistingStudent() {
        final boolean updated = students.update(200L, new Student("Vorname", "Nachname", 1));
        assertFalse(updated);
    }

    /**
     * Testcase für {@link StudentsMemory#delete(long)}.
     */
    @Test
    void testDeleteExistingStudent() {
        final boolean deleted = students.delete(1L);
        assertTrue(deleted);
        assertThat(students.getById(1L)).isNull();
        assertThat(students.getAll()).hasSize(2);
    }

    /**
     * Testcase für {@link StudentsMemory#delete(long)}.
     */
    @Test
    void testDeleteNotExistingStudent() {
        final boolean deleted = students.delete(100L);
        assertFalse(deleted);
        assertThat(students.getAll()).hasSize(3);
    }
}
