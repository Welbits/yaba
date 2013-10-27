package com.pilasvacias.yaba.screens.search;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;


/**
 * Created by Pablo Orgaz - 10/27/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
@MediumTest
public class SearchActivityTest extends ActivityUnitTestCase<SearchActivity> {


    private SearchActivity activity;

    public SearchActivityTest(Class<SearchActivity> activityClass) {
        super(SearchActivity.class);
    }

    @Override protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(),
                SearchActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
    }

    @LargeTest void hola() {
        assertEquals(0, 1);
    }
}
