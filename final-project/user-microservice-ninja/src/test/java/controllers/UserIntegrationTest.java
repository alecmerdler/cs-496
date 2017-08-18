package controllers;

import com.google.inject.Injector;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import dao.UserDao;
import models.Message;
import models.User;
import ninja.NinjaTest;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import services.MessageService;
import utils.UnirestObjectMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by alec on 11/7/16.
 */
public class UserIntegrationTest extends NinjaTest {

    ObjectMapper objectMapper;
    MessageService messageService;
    UserDao userDao;
    String apiUrl;
    String userUrl;
    String initializeUrl;

    @Before
    public void beforeEach() {
        Injector injector = getInjector();
        objectMapper = new ObjectMapper();
        messageService = injector.getInstance(MessageService.class);
        userDao = injector.getInstance(UserDao.class);
        apiUrl = getServerAddress() + "api/v1";
        userUrl = apiUrl + "/users";
        initializeUrl = getServerAddress() + "/initialize";

        Unirest.setObjectMapper(new UnirestObjectMapper());
    }

    @Test
    public void testCreateUserPublishesMessage() {
        messageService.subscribe("users", true)
                .subscribe((Message message) -> {
                    assertEquals("create", message.getAction());
                });
        User user = new User("johncleese", "johncleese@gmail.com");
        try {
            Unirest.post(initializeUrl).asJson();
            HttpResponse<JsonNode> response = Unirest.post(userUrl)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(user)
                    .asJson();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUserPublishesMessage() {
        messageService.subscribe("users", true)
                .subscribe((Message message) -> {
                    assertEquals("update", message.getAction());
                });
        User user = userDao.create(new User("johncleese", "johncleese@gmail.com"));
        try {
            Unirest.post(initializeUrl).asJson();
            HttpResponse<JsonNode> response = Unirest.put(userUrl + "/" + user.getId())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(user)
                    .asJson();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDestroyUserPublishesMessage() {
        messageService.subscribe("users", true)
                .subscribe((Message message) -> {
                    assertEquals("destroy", message.getAction());
                });
        User user = userDao.create(new User("johncleese", "johncleese@gmail.com"));
        try {
            Unirest.post(initializeUrl).asJson();
            HttpResponse<JsonNode> response = Unirest.delete(userUrl + "/" + user.getId())
                    .asJson();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
