package services;

import com.google.inject.Inject;
import dao.UserDao;
import models.User;
import org.hibernate.service.spi.ServiceException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Created by alec on 10/6/16.
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Inject
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> listAllUsers() {
        return userDao.findAll();
    }

    @Override
    public Optional<User> createUser(User user) throws ServiceException {
        final User createdUser;
        if (user == null) {
            throw new ServiceException("User should not be null");
        }
        try {
            createdUser = (User) userDao.create(user);
        } catch (PersistenceException pe) {
            throw new ServiceException(pe.getMessage());
        }

        return ofNullable(createdUser);
    }

    @Override
    public Optional<User> retrieveUserByUsername(String username) {
        if (username == null) {
            throw new ServiceException("Username should not be null");
        }
        User user = null;
        try {
            List<User> usersWithUsername = userDao.findByUsername(username);
            if (usersWithUsername.size() > 0) {
                user = usersWithUsername.get(0);
            }
        } catch (PersistenceException pe) {
            throw new ServiceException(pe.getMessage());
        }

        return ofNullable(user);
    }

    @Override
    public Optional<User> retrieveUserById(Long id) {
        if (id == null) {
            throw new ServiceException("ID should not be null");
        }
        User user = null;
        try {
            List<User> usersWithId = userDao.findById(id);
            if (usersWithId.size() > 0) {
                user = usersWithId.get(0);
            }
        } catch (PersistenceException pe) {
            throw new ServiceException(pe.getMessage());
        }

        return ofNullable(user);
    }

    @Override
    public Optional<User> updateUser(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("User should not be null");
        }
        final User updatedUser;
        try {
            updatedUser = userDao.update(user);
        } catch (PersistenceException pe) {
            throw new ServiceException(pe.getMessage());
        }

        return ofNullable(updatedUser);
    }

    @Override
    public boolean destroyUser(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("User should not be null");
        }
        boolean status = false;
        try {
            status = userDao.destroy(user);
        } catch (PersistenceException pe) {
            throw new ServiceException(pe.getMessage());
        }

        return status;
    }
}
