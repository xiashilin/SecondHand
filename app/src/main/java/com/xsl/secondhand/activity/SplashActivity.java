package com.xsl.secondhand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.xsl.secondhand.R;
import com.xsl.secondhand.utils.SharedPUtils;
import com.xsl.secondhand.utils.ioc.ViewById;
import com.xsl.secondhand.utils.ioc.ViewUtils;
import com.xsl.secondhand.widget.indicator.CirclePageIndicator;


/**
 * Created by xsl on 2016/5/20.
 */
public class SplashActivity extends AppCompatActivity {

    @ViewById(R.id.guideImage)
    ImageView guideImage;
    @ViewById(R.id.pager)
    ViewPager pager;
    @ViewById(R.id.indicator)
    CirclePageIndicator indicator;
    @ViewById(R.id.btnHome)
    Button btnHome;

    private int[] images = {
            R.mipmap.ic_guide_1,
            R.mipmap.ic_guide_2,
            R.mipmap.ic_guide_3
    };
    private GalleryPagerAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ay_splash);
        ViewUtils.inject(this);
        init();
    }

    public void init() {
        boolean firstTimeUse = (boolean) SharedPUtils.get(this, "first-time-use", true);
        if (firstTimeUse) {
            initGuideGallery();
        } else {
            initLaunchLogo();
        }
    }

    private void initLaunchLogo() {
        guideImage.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(false);
        // 旋转动画 从0到360 以中心旋转
        RotateAnimation rotateAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(1000);

        rotateAnim.setFillAfter(true);// 设置动画执行完毕后，控件停留在结束的状态

        // 缩放动画 0~1 从无到有 基于中心缩放

        ScaleAnimation scaleAnim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(1000);

        scaleAnim.setFillAfter(true);

        // 渐变动画 0~1 从没有到完全显示

        AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);

        alphaAnim.setDuration(2000);

        alphaAnim.setFillAfter(true);

        // 添加动画到集合
        animationSet.addAnimation(rotateAnim);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        guideImage.startAnimation(animationSet);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AVUser.getCurrentUser() != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 3000);
    }

    private void initGuideGallery() {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPUtils.put(SplashActivity.this, "first-time-use", false);
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });

        indicator.setVisibility(View.VISIBLE);
        pager.setVisibility(View.VISIBLE);

        adapter = new GalleryPagerAdapter();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.length - 1) {
                    btnHome.setVisibility(View.VISIBLE);
                    btnHome.startAnimation(fadeIn);
                } else {
                    btnHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public class GalleryPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView item = new ImageView(SplashActivity.this);
            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.setImageResource(images[position]);
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

}
