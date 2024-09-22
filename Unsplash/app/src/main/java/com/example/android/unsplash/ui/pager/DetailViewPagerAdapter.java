/*
 * Copyright (C) 2019 Google Inc. All Rights Reserved.
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
package com.example.android.unsplash.ui.pager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.android.unsplash.R;
import com.example.android.unsplash.data.model.Photo;
import com.example.android.unsplash.databinding.DetailViewBinding;
import com.example.android.unsplash.ui.DetailSharedElementEnterCallback;
import com.example.android.unsplash.ui.ImageSize;

import java.util.List;

/**
 * Adapter for paging detail views.
 */

public class DetailViewPagerAdapter extends PagerAdapter {

    private final List<Photo> allPhotos;
    private final LayoutInflater layoutInflater;
    private final int photoWidth;
    private final Activity host;
    private DetailSharedElementEnterCallback sharedElementCallback;

    public DetailViewPagerAdapter(@NonNull Activity activity, @NonNull List<Photo> photos,
                                  @NonNull DetailSharedElementEnterCallback callback) {
        layoutInflater = LayoutInflater.from(activity);
        allPhotos = photos;
        photoWidth = activity.getResources().getDisplayMetrics().widthPixels;
        host = activity;
        sharedElementCallback = callback;
    }

    @Override
    public int getCount() {
        return allPhotos.size();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        DetailViewBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.detail_view, container, false);
        binding.setData(allPhotos.get(position));
        onViewBound(binding);
        binding.executePendingBindings();
        container.addView(binding.getRoot());
        return binding;
    }

    private void onViewBound(DetailViewBinding binding) {
        Glide.with(host)
                .load(binding.getData().getPhotoUrl(photoWidth))
                .placeholder(R.color.placeholder)
                .override(ImageSize.NORMAL[0], ImageSize.NORMAL[1])
                .into(binding.photo);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof DetailViewBinding) {
            sharedElementCallback.setBinding((DetailViewBinding) object);
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object instanceof DetailViewBinding
                && view.equals(((DetailViewBinding) object).getRoot());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView(((DetailViewBinding) object).getRoot());
    }
}
