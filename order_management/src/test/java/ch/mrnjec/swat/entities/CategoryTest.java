package ch.mrnjec.swat.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    final void testGetterSetter(){
        final Category category = new Category(2);
        category.setName("Name");
        assertAll("Category", () -> assertThat(category.getId()).isEqualTo(2),
                () -> assertThat(category.getName()).isEqualTo("Name"));
    }

    @Test
    final void testEqualsObject() {
        EqualsVerifier.forClass(Category.class).withOnlyTheseFields("id").suppress(Warning.NONFINAL_FIELDS).verify();
    }
    /**
     * Test method for {@link Category}
     */
    @Test
    final void testToString() {
        final Category category = new Category(1, "Name");
        assertThat(category.toString()).contains("name");
    }
}