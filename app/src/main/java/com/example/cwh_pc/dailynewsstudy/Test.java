package com.example.cwh_pc.dailynewsstudy;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cwh_pc.dailynewsstudy.common.Constants;
import com.example.cwh_pc.dailynewsstudy.model.entities.MenuData;
import com.example.cwh_pc.dailynewsstudy.network.RetrofitNetWork;
import com.example.cwh_pc.dailynewsstudy.network.netapi.MenuService;
import com.trello.lifecycle2.android.lifecycle.RxLifecycleAndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Test {

    public void test(Context context) {
        GlideApp.with(context)
                .load(Constants.SPLASH_URL)
                .centerCrop()
                .placeholder(R.mipmap.splash)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                });
    }


}
