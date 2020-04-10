package ch.mrnjec.swat.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Testcases for {@link Student}.
 */
class StudentTest {

    /**
     * Test method for
     * {@link Student#Student(int, java.lang.String, java.lang.String, int)}.
     */
    @Test
    final void testStudentIntStringStringInt() {
        final Student student = new Student(1, "Vorname", "Nachname", 2);
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(1),
                    () -> assertThat(student.getFirstName()).isEqualTo("Vorname"),
                    () -> assertThat(student.getLastName()).isEqualTo("Nachname"),
                    () -> assertThat(student.getMonthOfBirth()).isEqualTo(2));
    }

    /**
     * Test method for {@link Student#Student(String, String, int)}.
     */
    @Test
    final void testStudentStringStringInt() {
        final Student student = new Student("Vorname", "Nachname", 1);
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(-1),
                    () -> assertThat(student.getFirstName()).isEqualTo("Vorname"),
                    () -> assertThat(student.getLastName()).isEqualTo("Nachname"),
                    () -> assertThat(student.getMonthOfBirth()).isEqualTo(1));
    }

    /**
     * Test method for {@link Student#Student()}.
     */
    @Test
    final void testStudentDefault() {
        final Student student = new Student();
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(-1),
                    () -> assertThat(student.getFirstName()).isEqualTo("unknown"),
                    () -> assertThat(student.getLastName()).isEqualTo("unknown"),
                    () -> assertThat(student.getMonthOfBirth()).isEqualTo(1));
    }

    /**
     * Test method for {@link Student#Student()}.
     */
    @Test
    final void testStudentDefaultSetter() {
        final Student student = new Student();
        student.setId(100);
        student.setFirstName("Vorname");
        student.setLastName("Nachname");
        student.setMonthOfBirth(5);
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(100),
                    () -> assertThat(student.getFirstName()).isEqualTo("Vorname"),
                    () -> assertThat(student.getLastName()).isEqualTo("Nachname"),
                    () -> assertThat(student.getMonthOfBirth()).isEqualTo(5));
    }

    /**
     * Test method for {@link Student#equals(java.lang.Object)}.
     */
    @Test
    final void testEqualsObject() {
        EqualsVerifier.forClass(Student.class).withOnlyTheseFields("id").suppress(Warning.NONFINAL_FIELDS).verify();
    }

    /**
     * Test method for {@link Student#toString()}.
     */
    @Test
    final void testToString() {
        assertThat(new Student(1, "Vorname", "Nachname", 2).toString()).contains("Vorname").contains("Nachname");
    }

}
