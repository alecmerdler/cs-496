package models;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by alec on 10/4/16.
 */

@Entity
public class User extends Model {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column()
    private int age;

    @Column()
    private String nickname;

    public User() {
        this.username = "";
        this.email = "";
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email, Long id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
