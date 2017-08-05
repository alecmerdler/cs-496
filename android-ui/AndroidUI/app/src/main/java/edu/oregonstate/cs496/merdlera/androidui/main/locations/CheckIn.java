package edu.oregonstate.cs496.merdlera.androidui.main.locations;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import edu.oregonstate.cs496.merdlera.androidui.BR;

/**
 * Created by alec on 8/4/17.
 */

public class CheckIn extends BaseObservable {

    private long id;
    private String comment = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }

    @Override
    public String toString() {
        return comment;
    }
}
