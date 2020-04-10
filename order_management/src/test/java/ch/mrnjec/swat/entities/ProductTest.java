package ch.mrnjec.swat.entities;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    /**
     * Test method for {@link Product}
     */
    @Test
    final void testProductSetterGetter() {
        ObjectId id = new ObjectId();
        final Product product = new Product(id, "Product", "Description", 55.0, "of", "dfdf");
        product.setName("Product");
        product.setDescription("Description");
        product.setPrice(55.0);
        Category category = new Category(1, "Category");
        product.setCategoryid("Id");

        assertAll("Product", () -> assertThat(product.getId()).isEqualTo(id),
                () -> assertThat(product.getName()).isEqualTo("Product"),
                () -> assertThat(product.getDescription()).isEqualTo("Description"),
                () -> assertThat(product.getPrice()).isEqualTo(55.0),
                () -> assertThat(product.getCategoryid()).isEqualTo("Id"));
    }

    /**
     * Test method for {@link Product#toString()}
     */
    @Test
    final void testToString() {
        final Product product = new Product(new ObjectId(), "test", "desc", 45.00, "of", "dfdf");
        product.setName("Product");
        product.setDescription("Description");
        product.setPrice(55.0);
        Category category = new Category(1, "Category");
        product.setCategoryid("dfdf");

        assertThat(product.toString()).contains("name").contains("description").contains("category");
    }

}