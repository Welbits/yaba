package com.pilasvacias.yaba.screens.alerts;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pilasvacias.yaba.R;

import java.util.Random;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 25/10/13.
 */
public class AlertsFragment extends Fragment {
    // Inject views
    @InjectView(R.id.dummy_textView)
    TextView textView;

    private static int randomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.argb(255, r, g, b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_dummy, container, false);
        Views.inject(this, rootView);
        String title = "Sólo disponible en la versión Pro";
        textView.setText(title);
        textView.setBackgroundColor(randomColor());
        return rootView;
    }
}
