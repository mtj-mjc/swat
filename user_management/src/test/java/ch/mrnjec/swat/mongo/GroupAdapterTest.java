package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Group;
import org.bson.types.ObjectId;

/**
 * Test Class for {@link GroupAdapter}
 *
 * @since: 21.05.2019
 * @author: matej
 */
public class GroupAdapterTest extends EntityAdapterExtendedTest<Group> {
    public static final ObjectId ID_TEST = new ObjectId();
    public static final String NAME_TEST = "Admin";

    @Override
    ObjectId getId() {
        return ID_TEST;
    }

    @Override
    String getName() {
        return NAME_TEST;
    }

    @Override
    Group getTestEntity() {
        return new Group(ID_TEST,NAME_TEST);
    }

    @Override
    Class<? extends EntityAdapterExtended<Group>> getAdapterClass() {
        return GroupAdapter.class;
    }
}
