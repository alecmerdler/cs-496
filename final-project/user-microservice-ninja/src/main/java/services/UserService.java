package services;

import models.User;
import org.hibernate.service.spi.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * Created by alec on 10/6/16.
 */
public interface UserService {

    List<User> listAllUsers();

    Optional<User> createUser(User user) throws ServiceException;

    Optional<User> retrieveUserById(Long id) throws ServiceException;

    Optional<User> retrieveUserByUsername(String username) throws ServiceException;

    Optional<User> updateUser(User user) throws ServiceException;

    boolean destroyUser(User user) throws ServiceException;
}
