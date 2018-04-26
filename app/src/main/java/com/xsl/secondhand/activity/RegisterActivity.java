package com.xsl.secondhand.activity;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.xsl.secondhand.R;
import com.xsl.secondhand.base.BaseActivity;
import com.xsl.secondhand.utils.SnackBarUtils;
import com.xsl.secondhand.utils.ToastUtils;
import com.xsl.secondhand.utils.ioc.OnClick;
import com.xsl.secondhand.utils.ioc.ViewById;
import com.xsl.secondhand.widget.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/24,Time:17:26
 * Description:
 */

public class RegisterActivity extends BaseActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.img_avatar)
    private CircleImageView mImgAvatar;
    /****请输入用户名****/
    @ViewById(R.id.et_username)
    private EditText mEtUsername;
    @ViewById(R.id.line)
    private View mLine;
    /****请输入密码****/
    @ViewById(R.id.et_password)
    private EditText mEtPassword;
    @ViewById(R.id.line_confirm)
    private View mLineConfirm;
    /****请确认密码****/
    @ViewById(R.id.et_confirm_password)
    private EditText mEtConfirmPassword;
    private byte[] mImageBytes = null;

    @ViewById(R.id.btn_register)
    private Button btn_register;
    @ViewById(R.id.register_progress)
    private ProgressBar progressBar;

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initEventAndData() {
        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_back);
        mToolbar.setTitle("注册");
    }

    @OnClick({R.id.button_submit_publish, R.id.btn_register})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_submit_publish:
                upload();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        if (mEtUsername.getText().toString().trim().length() < 5) {
            SnackBarUtils.show(mContext, "用户名格式不正确");
            return;
        }

        if (!ispsd(mEtPassword.getText().toString()) && mEtPassword.getText().toString().trim().length() < 5) {
            SnackBarUtils.show(mContext, "密码格式不正确");
            return;
        }
        if (!mEtPassword.getText().toString().equals(mEtConfirmPassword.getText().toString())) {
            SnackBarUtils.show(mContext, "两次密码不一致");
            return;
        }
        if (mImageBytes == null) {
            SnackBarUtils.show(mContext, "请选择一张照片");
            return;
        }
        btn_register.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(mEtUsername.getText().toString().trim());// 设置用户名
        user.setPassword(mEtPassword.getText().toString().trim());// 设置密码
        user.put("image", new AVFile("userHead", mImageBytes));
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                    ToastUtils.show(mContext, "注册成功");
                    RegisterActivity.this.finish();
                } else {
                    btn_register.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static boolean ispsd(String psd) {
        Pattern p = Pattern
                .compile("^[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher m = p.matcher(psd);

        return m.matches();
    }

    private void upload() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 24);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 24 && resultCode == RESULT_OK) {
            try {
                mImgAvatar.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
                mImageBytes = getBytes(getContentResolver().openInputStream(data.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
