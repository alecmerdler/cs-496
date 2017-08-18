package services;

import dao.UserDao;
import models.User;
import org.hibernate.service.spi.ServiceException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by alec on 10/6/16.
 */
public class UserServiceImplTest {

    UserServiceImpl userService;

    // Mocks
    UserDao userDaoMock;

    @Before
    public void beforeEach() {
        userDaoMock = mock(UserDao.class);
    }

    @Test
    public void testListAllUsersNoUsers() {
        doReturn(new ArrayList<>()).when(userDaoMock).findAll();
        userService = new UserServiceImpl(userDaoMock);

        assertEquals(0, userService.listAllUsers().size());
        verify(userDaoMock).findAll();
    }

    @Test
    public void testListAllUsersSomeUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        doReturn(users).when(userDaoMock).findAll();
        userService = new UserServiceImpl(userDaoMock);

        assertEquals(users.size(), userService.listAllUsers().size());
        verify(userDaoMock).findAll();
    }

    @Test
    public void testCreateUserValid() {
        User user = new User();
        doReturn(user).when(userDaoMock).create(user);
        userService = new UserServiceImpl(userDaoMock);

        Optional<User> userOptional = userService.createUser(user);
        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
        verify(userDaoMock).create(user);
    }

    @Test
    public void testCreateUserInvalid() {
        userService = new UserServiceImpl(userDaoMock);
        try {
            userService.createUser(null);
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof ServiceException);
        }
    }

    @Test
    public void testRetrieveUserByUsernameDoesNotExist() {
        String username = "bob";
        doReturn(new ArrayList<>()).when(userDaoMock).findByUsername(username);
        userService = new UserServiceImpl(userDaoMock);

        assertFalse(userService.retrieveUserByUsername(username).isPresent());
        verify(userDaoMock).findByUsername(username);
    }

    @Test
    public void testRetrieveUserByUsernameExists() {
        User user = new User("bob", "bob@gmail.com");
        List<User> usersWithUsername = new ArrayList<>();
        usersWithUsername.add(user);
        doReturn(usersWithUsername).when(userDaoMock).findByUsername(user.getUsername());
        userService = new UserServiceImpl(userDaoMock);

        assertTrue(userService.retrieveUserByUsername(user.getUsername()).isPresent());
        verify(userDaoMock).findByUsername(user.getUsername());
    }

    @Test
    public void testRetrieveUserByIdDoesNotExist() {
        Long id = new Long(1);
        doReturn(new ArrayList<>()).when(userDaoMock).findById(id);
        userService = new UserServiceImpl(userDaoMock);

        assertFalse(userService.retrieveUserById(id).isPresent());
        verify(userDaoMock).findById(id);
    }

    @Test
    public void testRetrieveUserByIdExists() {
        User user = new User("bob", "bob@gmail.com", new Long(1));
        List<User> usersWithUsername = new ArrayList<>();
        usersWithUsername.add(user);
        doReturn(usersWithUsername).when(userDaoMock).findById(user.getId());
        userService = new UserServiceImpl(userDaoMock);

        assertTrue(userService.retrieveUserById(user.getId()).isPresent());
        verify(userDaoMock).findById(user.getId());
    }

    @Test
    public void testUpdateUserNull() {
        userService = new UserServiceImpl(userDaoMock);
        try {
            userService.updateUser(null);
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof ServiceException);
        }
    }

    @Test
    public void testUpdateUserExists() {
        User user = new User("bob", "bob@gmail.com");
        doReturn(user).when(userDaoMock).update(user);
        userService = new UserServiceImpl(userDaoMock);

        assertTrue(userService.updateUser(user).isPresent());
        verify(userDaoMock).update(user);
    }

    @Test
    public void testUpdateUserDoesNotExist() {
        User user = new User("bob", "bob@gmail.com");
        doThrow(new PersistenceException("User with given ID does not exist")).when(userDaoMock).update(user);
        userService = new UserServiceImpl(userDaoMock);

        try {
            Optional<User> userOptional = userService.updateUser(user);
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof ServiceException);
            verify(userDaoMock).update(user);
        }
    }

    @Test
    public void testDestroyUserExists() {
        User user = new User("bob", "bob@gmail.com");
        doReturn(true).when(userDaoMock).destroy(user);
        userService = new UserServiceImpl(userDaoMock);

        assertTrue(userService.destroyUser(user));
        verify(userDaoMock).destroy(user);
    }
}