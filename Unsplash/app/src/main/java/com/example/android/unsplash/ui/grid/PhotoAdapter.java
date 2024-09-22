/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.example.android.unsplash.ui.grid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.unsplash.R;
import com.example.android.unsplash.data.model.Photo;
import com.example.android.unsplash.databinding.PhotoItemBinding;
import com.example.android.unsplash.ui.ImageSize;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private final ArrayList<Photo> photos;
    private final int requestedPhotoWidth;
    private final LayoutInflater layoutInflater;

    public PhotoAdapter(@NonNull Context context, @NonNull ArrayList<Photo> photos) {
        this.photos = photos;
        requestedPhotoWidth = context.getResources().getDisplayMetrics().widthPixels;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new PhotoViewHolder((PhotoItemBinding) DataBindingUtil.inflate(layoutInflater,
                R.layout.photo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        PhotoItemBinding binding = holder.getBinding();
        Photo data = photos.get(position);
        binding.setData(data);
        binding.executePendingBindings();
        Glide.with(layoutInflater.getContext())
                .load(holder.getBinding().getData().getPhotoUrl(requestedPhotoWidth))
                .placeholder(R.color.placeholder)
                .override(ImageSize.NORMAL[0], ImageSize.NORMAL[1])
                .into(holder.getBinding().photo);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public long getItemId(int position) {
        return photos.get(position).id;
    }
}
