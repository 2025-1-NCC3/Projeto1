package com.umonitoring.components;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.umonitoring.R;
import com.umonitoring.activities.ListaViagemActivity;
import com.umonitoring.activities.InfoPage;
import com.umonitoring.activities.TelaDeViagemActivity;

public class BottomNavHelper {

    public static void setupBottomNavigation(Activity activity, int selectedItemId) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.nav_home) {
                intent = new Intent(activity, TelaDeViagemActivity.class);
            } else if (itemId == R.id.nav_info) {
                intent = new Intent(activity, ListaViagemActivity.class);
            } else if (itemId == R.id.nav_config) {
                intent = new Intent(activity, InfoPage.class);
            }

            if (intent != null) {
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }
}
