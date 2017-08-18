package dao;

import com.google.inject.Provider;
import models.Model;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by alec on 10/6/16.
 */
public class BaseDaoTest {

    Dao baseDao;

    // Mocks
    Provider<EntityManager> providerMock;
    EntityManager entityManagerMock;
    Query queryMock;
    Model modelMock = new ModelMock();

    private class ModelMock extends Model {

    }

    @Before
    public void beforeEach() {
        entityManagerMock = mock(EntityManager.class);
        providerMock = mock(Provider.class);
        queryMock = mock(Query.class);
        doReturn(entityManagerMock).when(providerMock).get();
        baseDao = new BaseDao(providerMock, modelMock.getClass().getSimpleName(), modelMock.getClass());
    }

    @Test
    public void testFindAllNoModels() {
        String queryString = "select t from" + modelMock.getClass().getSimpleName() + " as t";
        doReturn(queryMock).when(entityManagerMock).createQuery("select t from " + modelMock.getClass().getSimpleName() + " as t");
        doReturn(new ArrayList<>()).when(queryMock).getResultList();

        assertEquals(0, baseDao.findAll().size());
    }

    @Test
    public void testFindAllSomeModels() {
        List<ModelMock> models = new ArrayList<>();
        models.add(new ModelMock());
        models.add(new ModelMock());
        String queryString = "select t from" + modelMock.getClass().getSimpleName() + " as t";
        doReturn(queryMock).when(entityManagerMock).createQuery("select t from " + modelMock.getClass().getSimpleName() + " as t");
        doReturn(models).when(queryMock).getResultList();

        assertEquals(models.size(), baseDao.findAll().size());
    }

    @Test
    public void testCreateValid() {
        ModelMock model = new ModelMock();
        doNothing().when(entityManagerMock).persist(model);

        assertEquals(model, baseDao.create(model));
        verify(entityManagerMock).persist(model);
    }

    @Test
    public void testCreateInvalidThrowsPersistenceException() {
        try {
            baseDao.create(null);
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof PersistenceException);
        }
    }

}