package com.pilasvacias.yaba;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayOutputStream;

import timber.log.Timber;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        Serializer serializer = new Persister();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            serializer.write(new Test(), out);
            Timber.DEBUG.d("-\n%s", new String(out.toByteArray()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Default
    @Root
    private static class Test {
        int x = 3;
        int y = 9;
    }

}
