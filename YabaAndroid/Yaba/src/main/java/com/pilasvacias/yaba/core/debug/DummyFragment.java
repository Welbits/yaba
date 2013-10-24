package com.pilasvacias.yaba.core.debug;

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

public class DummyFragment extends Fragment {
    // Constants
    private static final String TITLE_KEY = "title";
    // Inject views
    @InjectView(R.id.dummy_textView)
    TextView textView;
    // Fields
    private String title;

    public DummyFragment() {
        super();
    }

    public static DummyFragment newInstance(String title) {
        DummyFragment dummyFragment = new DummyFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        dummyFragment.setArguments(args);
        return dummyFragment;
    }

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
        title = getArguments().getString(TITLE_KEY);
        textView.setText(title);
        textView.setBackgroundColor(randomColor());
        return rootView;
    }
}