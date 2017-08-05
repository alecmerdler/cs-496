package edu.oregonstate.cs496.merdlera.androidui.main.locations;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.oregonstate.cs496.merdlera.androidui.R;
import edu.oregonstate.cs496.merdlera.androidui.databinding.ActivityLocationsBinding;
import edu.oregonstate.cs496.merdlera.androidui.databinding.CheckinListItemViewBinding;

import java.util.List;

/**
 * Created by alec on 8/3/17.
 */

public class LocationActivity extends AppCompatActivity {

    private CheckIn newCheckIn;
    private CheckInDataSource dataSource;
    private ListView checkInListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        // Initialize new check-in form
        newCheckIn = new CheckIn();

        // Initialize data source
        dataSource = new CheckInDataSource(this);
        dataSource.open();

        // Populate existing check-in list
        loadCheckIns();

        // Set up data binding
        ActivityLocationsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_locations);
        binding.setNewCheckIn(newCheckIn);
    }

    public void onSubmit(View view) {
        if (newCheckIn.getComment().length() > 0) {
            dataSource.createCheckIn(newCheckIn);
            newCheckIn.setComment("");

            // Retrieve updated list of check-ins
            loadCheckIns();
        }
    }

    private void loadCheckIns() {
        List<CheckIn> allCheckIns = dataSource.getAllCheckIns();
        checkInListView = (ListView) findViewById(R.id.listview);
        checkInListView.setAdapter(new CheckInArrayAdapter(this, allCheckIns));
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    private class CheckInArrayAdapter extends ArrayAdapter<CheckIn> {

        private final Context context;
        private final List<CheckIn> checkIns;

        public CheckInArrayAdapter(Context context, List<CheckIn> checkIns) {
            super(context, -1, checkIns);
            this.context = context;
            this.checkIns = checkIns;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CheckinListItemViewBinding binding = CheckinListItemViewBinding.inflate(inflater, parent, false);
            binding.setCheckIn(checkIns.get(position));

            return binding.getRoot();
        }
    }
}
