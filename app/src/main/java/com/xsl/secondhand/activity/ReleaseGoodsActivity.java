package com.xsl.secondhand.activity;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.xsl.secondhand.R;
import com.xsl.secondhand.base.BaseActivity;
import com.xsl.secondhand.utils.SnackBarUtils;
import com.xsl.secondhand.utils.ToastUtils;
import com.xsl.secondhand.utils.ioc.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/23,Time:22:20
 * Description:
 */

public class ReleaseGoodsActivity extends BaseActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;


    /****标题****/
    @ViewById(R.id.edittext_title_publish)
    private EditText mEdittextTitlePublish;
    /****商品描述****/
    @ViewById(R.id.edittext_discription_publish)
    private EditText mEdittextDiscriptionPublish;
    /****金额****/
    @ViewById(R.id.edittext_price_publish)
    private EditText mEdittextPricePublish;
    /****选择图片****/
    @ViewById(R.id.button_select_publish)
    private ImageView mImgSelectPublish;
    @ViewById(R.id.imageview_select_publish)
    private ImageView mImageviewSelectPublish;
    /****发布****/
    @ViewById(R.id.button_submit_publish)
    private Button mButtonSubmitPublish;
    @ViewById(R.id.mProgess)
    private ProgressBar mMProgess;
    private byte[] mImageBytes = null;

    @Override
    protected int getLayout() {
        return R.layout.ay_release_goods;
    }

    @Override
    protected void initEventAndData() {
        initToolBar();
        mImgSelectPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 42);
            }
        });

        mButtonSubmitPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(mEdittextTitlePublish.getText().toString()) && mEdittextTitlePublish.getText().toString().length() < 5) {
                    SnackBarUtils.show(mContext, "标题格式不正确");
                    return;
                }
                if ("".equals(mEdittextDiscriptionPublish.getText().toString()) && mEdittextDiscriptionPublish.getText().toString().length() < 10) {
                    SnackBarUtils.show(mContext, "商品描述格式不正确");
                    return;
                }
                if ("".equals(mEdittextPricePublish.getText().toString())) {
                    SnackBarUtils.show(mContext, "请输入金额");
                    return;
                }
                if (mImageBytes == null) {
                    SnackBarUtils.show(mContext, "请选择一张照片");
                    return;
                }
                mMProgess.setVisibility(View.VISIBLE);
                AVObject product = new AVObject("Product");
                product.put("title", mEdittextTitlePublish.getText().toString());
                product.put("description", mEdittextDiscriptionPublish.getText().toString());
                product.put("price", Integer.parseInt(mEdittextPricePublish.getText().toString()));
                product.put("owner", AVUser.getCurrentUser());
                product.put("image", new AVFile("productPic", mImageBytes));
                product.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            mMProgess.setVisibility(View.GONE);
                            ToastUtils.show(mContext, "发布成功！");
                            mContext.finish();
                        } else {
                            mMProgess.setVisibility(View.GONE);
                            SnackBarUtils.show(mContext, e.getMessage());
                        }
                    }
                });
//        }, mImageUploadProgressCallback);
            }
        });
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_back);
        mToolbar.setTitle("发布我的物品");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 42 && resultCode == RESULT_OK) {
            try {
                mImageviewSelectPublish.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
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
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
