package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alec on 10/11/16.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meal extends Model {

    @Column(nullable = false)
    private String mealName;

    @Column(nullable = false)
    private Long chefId;

    @Column()
    private String description;

    @Column()
    private String taskId;

    @Column()
    private String taskListId;

    @ManyToMany(cascade = CascadeType.ALL,
                fetch = FetchType.EAGER)
    @JoinTable(name="meal_tag",
               joinColumns = @JoinColumn(name = "meal_id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    public Meal() {
        this.mealName = "";
        this.chefId = new Long(1);
        this.tags = new ArrayList<>();
    }

    public Meal(String mealName) {
        this.mealName = mealName;
        this.chefId = new Long(1);
        this.tags = new ArrayList<>();
    }

    public Meal(String mealName, Long chefId) {
        this.mealName = mealName;
        this.chefId = chefId;
        this.tags = new ArrayList<>();
    }

    public Meal(String mealName, Long chefId, List<Tag> tags) {
        this.mealName = mealName;
        this.chefId = chefId;
        this.tags = tags;
    }

    public Meal(String mealName, Long chefId, List<Tag> tags, Long id) {
        this.mealName = mealName;
        this.chefId = chefId;
        this.tags = tags;
        this.id = id;
    }

    public Meal(String mealName, String description, Long chefId, List<Tag> tags, Long id) {
        this.mealName = mealName;
        this.description = description;
        this.chefId = chefId;
        this.tags = tags;
        this.id = id;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public Long getChefId() {
        return this.chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(String taskListId) {
        this.taskListId = taskListId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Meal addTag(Tag tag) {
        tags.add(tag);

        return this;
    }
}
