package edu.oregonstate.cs496.merdlera.androidui.main.locations;

/**
 * Created by alec on 8/4/17.
 */

public class CheckIn {

    private long id;
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return comment;
    }
}
