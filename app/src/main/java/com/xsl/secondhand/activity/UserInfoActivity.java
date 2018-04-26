package com.xsl.secondhand.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.squareup.picasso.Picasso;
import com.xsl.secondhand.R;
import com.xsl.secondhand.base.BaseActivity;
import com.xsl.secondhand.bean.Constant;
import com.xsl.secondhand.utils.ImageUtils;
import com.xsl.secondhand.utils.SharedPUtils;
import com.xsl.secondhand.utils.ioc.OnClick;
import com.xsl.secondhand.utils.ioc.ViewById;
import com.xsl.secondhand.widget.SettingItemView;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gy on 2017/12/24.
 * 用户信息管理中心
 */
public class UserInfoActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.rlt_update_icon)
    RelativeLayout iconRL;
    @ViewById(R.id.img_icon)
    ImageView iconIv;
    @ViewById(R.id.username)
    SettingItemView usernameCL;
    @ViewById(R.id.sex)
    SettingItemView sexCL;
    @ViewById(R.id.phone)
    SettingItemView phoneCL;
    @ViewById(R.id.email)
    SettingItemView emailCL;


    //选择图片来源
    private AlertDialog iconDialog;
    private AlertDialog genderDialog;
    private AlertDialog phoneDialog;
    private AlertDialog emailDialog;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int GENDER_MAN = 0;
    protected static final int GENDER_FEMALE = 1;
    private static final int CROP_SMALL_PICTURE = 2;

    //图片路径
    protected static Uri tempUri = null;

    @Override
    protected int getLayout() {
        return R.layout.ay_user_info;
    }

    @Override
    protected void initEventAndData() {

        //初始化Toolbar

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_back);
        toolbar.setTitle("账户信息");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回消息更新上个Activity数据
                setResult(RESULT_OK);
                finish();
            }
        });
        refrashData();

    }

    /**
     * 监听点击事件
     *
     * @param view
     */
    @OnClick({R.id.rlt_update_icon, R.id.btn_exist})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.rlt_update_icon:  //头像
                showIconDialog();
                break;
            case R.id.btn_exist:
                setResult(RESULT_OK);
                AVUser.logOut();
                finish();
                break;
        }
    }

    /**
     * 显示选择头像来源对话框
     */
    public void showIconDialog() {
        if (iconDialog == null) {
            iconDialog = new AlertDialog.Builder(this).setItems(new String[]{"相册", "相机"},
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case CHOOSE_PICTURE: // 选择本地照片
                                    Intent openAlbumIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    openAlbumIntent.setType("image/*");
                                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    break;
                                case TAKE_PICTURE: // 拍照
                                    takePicture();
                                    break;
                            }
                        }
                    }).create();
        }
        if (!iconDialog.isShowing()) {
            iconDialog.show();
        }
    }


    /**
     * 监听Activity返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(intent.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (intent != null) {
                        setImageToView(intent); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
            setResult(RESULT_OK, new Intent());
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 拍照
     */
    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(UserInfoActivity.this,
                    "com.copasso.cocobill.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            iconIv.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    /**
     * 保存头像并上传服务器
     *
     * @param bitmap
     */
    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagename = Constant.currentUserId + "_" + String.valueOf(System.currentTimeMillis());
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), imagename + ".png");
        currentUser.setImage(imagename + ".png");
        SharedPUtils.setCurrentUser(UserInfoActivity.this, currentUser);
        if (imagePath != null) {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            File file = new File(imagePath);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", imagename + ".png",
                            RequestBody.create(MediaType.parse("image/png"), file))
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization", "Client-ID " + "...")
                    .url("http://139.199.176.173:8080/ssmBillBook/upload/")
                    .post(requestBody)
                    .build();

            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        }
    }

    /**
     * 权限请求
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void refrashData() {
        if (AVUser.getCurrentUser() != null) {
            sexCL.setLeftText("性别:                      " + "男");
            phoneCL.setLeftText("电话:            " + "1856585255552");
            emailCL.setLeftText("邮箱:            " + "15244221656@qq.com");
            usernameCL.setLeftText("用户名:                      " + AVUser.getCurrentUser().getUsername());
            Picasso.with(mContext).load(AVUser.getCurrentUser().getAVFile("image") == null ? "www" : AVUser.getCurrentUser().getAVFile("image").getUrl()).into(iconIv);
        }
    }
}
