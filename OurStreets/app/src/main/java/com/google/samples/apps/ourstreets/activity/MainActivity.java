/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.ourstreets.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.google.samples.apps.ourstreets.fragment.BackPressAware;
import com.google.samples.apps.ourstreets.fragment.GalleryFragment;

import com.firebase.client.Config;
import com.firebase.client.Firebase;

/**
 * The activity hosting all fragments for this application.
 */
public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebase();
        // FIXME: This call is necessary when not calling setContentView.
        getDelegate().onPostCreate(null);
        findViewById(android.R.id.content)
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .replace(android.R.id.content, getGalleryFragment(), GalleryFragment.TAG)
                    .commit();
        }
    }

    private void initFirebase() {
        Firebase.setAndroidContext(getApplicationContext());
        Config defaultConfig = Firebase.getDefaultConfig();
        if (!defaultConfig.isFrozen()) {
            defaultConfig.setPersistenceEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        /** @see BackPressAware */
        for (Fragment fragment : mFragmentManager.getFragments()) {
            if (fragment instanceof BackPressAware && fragment.isAdded()) {
                // Enable a single fragment to intercept the back press.
                ((BackPressAware) fragment).onBackPressed();
                return;
            }
        }
        // Intercept all back press calls until there's no more fragments on the back stack.
        mFragmentManager.popBackStack();
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
    }

    @NonNull
    private GalleryFragment getGalleryFragment() {
        GalleryFragment fragment = (GalleryFragment) mFragmentManager
                .findFragmentByTag(GalleryFragment.TAG);
        if (fragment == null) {
            fragment = GalleryFragment.newInstance();
        }
        return fragment;
    }
}
