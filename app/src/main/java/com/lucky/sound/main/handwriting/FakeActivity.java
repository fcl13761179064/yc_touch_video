package com.lucky.sound.main.handwriting;

import android.app.Activity;
import android.content.pm.ApplicationInfo;

public class FakeActivity extends Activity {
    @Override
    public ApplicationInfo getApplicationInfo() {
        return new FakeApplicationInfo();
    }
}
