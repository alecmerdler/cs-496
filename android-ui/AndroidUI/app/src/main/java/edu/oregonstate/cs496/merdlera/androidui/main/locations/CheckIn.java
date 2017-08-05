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
    private String latitude = "";
    private String longitude = "";

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

    @Bindable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);
    }

    @Bindable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.longitude);
    }

    @Override
    public String toString() {
        return comment;
    }
}
