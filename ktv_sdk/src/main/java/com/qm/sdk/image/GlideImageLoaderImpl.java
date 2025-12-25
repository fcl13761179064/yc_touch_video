package com.qm.sdk.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tme.ktv.api.IKImageLoader;
import com.tme.ktv.api.ImageLoaderOption;

public class GlideImageLoaderImpl implements IKImageLoader {

    @Override
    public void displayImage(int imageResId, ImageView imageView) {
        Glide.with(imageView).load(getDrawable(imageResId, imageView)).into(imageView);
    }

    @Override
    public void displayImage(String imageUrl, ImageView imageView) {
        Glide.with(imageView).load(imageUrl).into(imageView);
    }

    public void displayImage(String imageUrl, ImageView imageView, int placeHolderResId) {
        Glide.with(imageView).load(imageUrl).placeholder(getDrawable(placeHolderResId, imageView)).into(imageView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void displayImage(String imageUrl, ImageView imageView, ImageLoaderOption option) {
        if (option == null) {
            option = new ImageLoaderOption();
        }
        int radius = option.getRadius();
        boolean isRound = option.isRoundImage();
        RequestOptions options = new RequestOptions();
        if (option.getPlaceHolder() != null) {
            options.placeholder(option.getPlaceHolder());
        } else {
            options.placeholder(option.getPlaceHolderResId());
        }
        if (radius > 0) {
            options.transform(new CenterCrop(), new RoundedCorners(radius));
        } else if (isRound) {
            options.transform(new CircleCrop());
        }
        Glide.with(imageView)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void displayBackgroundImage(String imageUrl, View view, ImageLoaderOption option) {
        if (option == null) {
            option = new ImageLoaderOption();
        }
        int radius = option.getRadius();
        boolean isRound = option.isRoundImage();
        RequestOptions options = new RequestOptions();
        if (option.getPlaceHolder() != null) {
            options.placeholder(option.getPlaceHolder());
        } else {
            options.placeholder(option.getPlaceHolderResId());
        }
        if (radius > 0) {
            options.transform(new CenterCrop(), new RoundedCorners(radius));
        } else if (isRound) {
            options.transform(new CircleCrop());
        }
        Glide.with(view)
                .load(imageUrl)
                .apply(options)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    private Drawable getDrawable(int imageResId, ImageView imageView) {
        return imageView.getContext().getResources().getDrawable(imageResId);
    }
}
