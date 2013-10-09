package com.pilasvacias.yaba.screens;

import android.os.Bundle;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.common.network.NetworkActivity;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayOutputStream;

public class MainActivity extends NetworkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Serializer serializer = new Persister();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            serializer.write(new Test(), out);
            getTimber().e("-\n%s", new String(out.toByteArray()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Default
    @Root
    private static class Test {
        int x = 3;
        int y = 9;
    }

}
