package com.umonitoring.components;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.umonitoring.R;
import com.umonitoring.activities.MainActivity;
import com.umonitoring.activities.TelaDeViagemActivity;
import com.umonitoring.activities.TesteActivity;


public class BottomNavBar {

    private BottomNavigationView bottomNavigationView;

    public BottomNavBar(Activity activity) {
        bottomNavigationView = activity.findViewById(R.id.bottom_navigation);


        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    activity.startActivity(new Intent(activity, TelaDeViagemActivity.class));
                    return true;

                } else if (itemId == R.id.nav_activity) {
                    activity.startActivity(new Intent(activity, TesteActivity.class));
                    return true;

                } else if (itemId == R.id.nav_account) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    return true;
                }

                return false;
            });
        }
    }
}
