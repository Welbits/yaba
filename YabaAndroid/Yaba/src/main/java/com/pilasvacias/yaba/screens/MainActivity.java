package com.pilasvacias.yaba.screens;

import android.os.Bundle;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.common.network.NetworkActivity;
import com.pilasvacias.yaba.modules.soap.EnvelopeSerializer;
import com.pilasvacias.yaba.modules.util.L;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends NetworkActivity {

    @Inject EnvelopeSerializer envelopeSerializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityGraph().inject(this);

        Test in = new Test();
        in.lista.add(new Otro());
        in.lista.add(new Otro());
        String xml1 = envelopeSerializer.toXML(in);
        Test rec = envelopeSerializer.fromXML(xml1, Test.class);
        String xml2 = envelopeSerializer.toXML(rec);

        L.og.w("-\n%s", xml1);
        L.og.w("-\n%s", xml2);
        L.og.e("ok -> %b", xml1.equalsIgnoreCase(xml2));


    }

    @Default
    @Root
    private static class Test extends Base {
        int x = 3;
        int y = 9;
        List<Otro> lista = new ArrayList<Otro>();
    }

    @Default
    @Root
    private static class Otro {
        static int x = 0;
        String elem_t = "===============";
    }

    @Default
    @Root
    private static class Base {
        int auth = 335;
        String key = "key";
    }


}
