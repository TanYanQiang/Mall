package com.tgh.devkit.core;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.RenamingDelegatingContext;

/**
 * Created by albert on 16/1/28.
 */
public class BaseTest {

    protected final Context context;

    public BaseTest() {
        context = new RenamingDelegatingContext(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                "test_");
    }

    public Context getContext() {
        return context;
    }
}
