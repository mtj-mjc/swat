package ch.mrnjec.swat.entities;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private static final String PRODUCT = "Product";
    private static final String DESCRIPTION = "Description";

    /**
     * Test method for {@link Product}
     */
    @Test
    final void testProductSetterGetter() {
        ObjectId id = new ObjectId();
        final Product product = new Product(id, PRODUCT, DESCRIPTION, 55.0, "of", "dfdf");
        product.setName(PRODUCT);
        product.setDescription(DESCRIPTION);
        product.setPrice(55.0);
        product.setCategoryid("Id");

        assertAll(PRODUCT, () -> assertThat(product.getId()).isEqualTo(id),
                () -> assertThat(product.getName()).isEqualTo(PRODUCT),
                () -> assertThat(product.getDescription()).isEqualTo(DESCRIPTION),
                () -> assertThat(product.getPrice()).isEqualTo(55.0),
                () -> assertThat(product.getCategoryid()).isEqualTo("Id"));
    }

    /**
     * Test method for {@link Product#toString()}
     */
    @Test
    final void testToString() {
        final Product product = new Product(new ObjectId(), "test", "desc", 45.00, "of", "dfdf");
        product.setName(PRODUCT);
        product.setDescription(DESCRIPTION);
        product.setPrice(55.0);
        product.setCategoryid("dfdf");

        assertThat(product.toString()).contains("name").contains("description").contains("category");
    }
}