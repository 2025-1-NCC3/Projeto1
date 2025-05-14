package com.umonitoring.components;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.umonitoring.R;
import com.umonitoring.activities.ListaViagemActivity;
import com.umonitoring.activities.ProfilePage;
import com.umonitoring.activities.TelaDeViagemActivity;

public class BottomNavHelper {

    public static void setupBottomNavigation(Activity activity, int selectedItemId) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home && selectedItemId != R.id.nav_home) {
                Intent intent = new Intent(activity, TelaDeViagemActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_info && selectedItemId != R.id.nav_info) {
                Intent intent = new Intent(activity, ListaViagemActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_config && selectedItemId != R.id.nav_config) {
                Intent intent = new Intent(activity, ProfilePage.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }
}
