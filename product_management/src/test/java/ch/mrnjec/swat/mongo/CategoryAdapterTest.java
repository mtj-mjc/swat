package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Category;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

/**
 * Test Class for {@link CategoryAdapter}
 *
 * @since: 23.05.2019
 * @author: Matej Mrnjec
 */
class CategoryAdapterTest extends EntityAdapterExtendedTest<Category> {
    public static final String TEST_STR = "Test";
    public final static String NOID = "000000000000000000000000";
    public static final ObjectId TEST_ID = new ObjectId(NOID);

    /**
     * Gets a ObjectId for Tests
     *
     * @return ObjectId
     */
    @Override
    ObjectId getId() {
        return TEST_ID;
    }

    /**
     * Gets a Name for Tests
     *
     * @return Name
     */
    @Override
    String getName() {
        return TEST_STR;
    }

    /**
     * Gets a instance of Entity for Tests
     *
     * @return Entity
     */
    @Override
    Category getTestEntity() {
        return new Category(TEST_ID, TEST_STR);
    }

    /**
     * Get List of Entities for Tests
     *
     * @return List of Entities
     */
    @Override
    List<Category> getTestEntities() {
        return Arrays.asList(new Category(TEST_ID, TEST_STR)
                ,new Category(TEST_ID, TEST_STR));
    }

    @Override
    Class<? extends EntityAdapterExtended<Category>> getAdapterClass() {
        return CategoryAdapter.class;
    }
}