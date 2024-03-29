package io.zhuliang.appchooser.data.local;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.runner.RunWith;

import io.zhuliang.appchooser.data.ActivityInfosDataSource;

/**
 * @author Zhu Liang
 * @version 1.0
 * @since 2017/5/5 下午4:08
 */
@RunWith(AndroidJUnit4.class)
public class ActivityInfosSharedPreferencesDataSourceTest extends BaseActivityInfosDataSourceTest {

    @NonNull
    @Override
    ActivityInfosDataSource getDataSource() {
        return new ActivityInfosSharedPreferencesDataSource(
                InstrumentationRegistry.getContext().getApplicationContext());
    }
}