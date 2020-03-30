package com.wondersgroup.cardverification.mvp.activity.camera;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wondersgroup.cardverification.R;
import com.wondersgroup.cardverification.utils.GlideImageUtils;
import com.wondersgroup.cardverification.utils.io.FaceBase64Util;
import com.wondersgroup.cardverification.utils.io.FileUrlUtils;
import com.wondersgroup.cardverification.widget.camera.CameraConfig;
import com.wondersgroup.cardverification.widget.camera.CropActivity;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/camera/TestCameraActivity")
public class TestCameraActivity extends AppCompatActivity {
    public static final int CODE_TAKE_PHOTO = 1;//使用拍照获取的图片
    public static final int CODE_SELECT_PIC = 2;//使用相册中的图片
    public static final int CODE_CROP_PIC = 3;//使用带裁剪框的图片

    @BindView(R.id.tv_call_card)
    TextView tvCallCard;
    @BindView(R.id.tv_call_camera)
    TextView tvCallCamera;
    @BindView(R.id.tv_call_photo)
    TextView tvCallPhoto;
    @BindView(R.id.tv_path)
    TextView tvPath;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;

    private Uri mPhotoUri;//拍照后图片的uri
    private String mPicPath;//选择相册图片路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);
        ButterKnife.bind(this);
        //扫描卡
        tvCallCard.setOnClickListener(v -> takePhoto());
        //调用相机
        tvCallCamera.setOnClickListener(v -> {
            /**
             * 相机拍照,照片默认保存在相册中,好处是: 获取的图片是拍照后的原图,不使用contentValues存放路径的话,拍照后的图片为缩略,可能不太清晰
             */
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues contentValues = new ContentValues();
            mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            startActivityForResult(intent, CODE_TAKE_PHOTO);
        });
        //调用相册
        tvCallPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, CODE_SELECT_PIC);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_SELECT_PIC) { //选择图片
                if (null != data && null != data.getData()) {
                    mPhotoUri = data.getData();
                    mPicPath = convertUriToFilePath(mPhotoUri);
                    //tvPath.setText(mPicPath);
                    //GlideImageUtils.roundedCornersImage(this, ivPicture, mPicPath, 8, R.mipmap.wel1);
                }
            } else if (requestCode == CODE_TAKE_PHOTO) { //拍照
                String[] pojo = {MediaStore.Images.Media.DATA};
                if (mPhotoUri == null) return;
                Cursor cursor = getContentResolver().query(mPhotoUri, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    mPicPath = cursor.getString(columnIndex);
                    cursor.close();
                }
            }else if (requestCode == CODE_CROP_PIC) {
                mPicPath = data.getStringExtra(CameraConfig.IMAGE_PATH);
                //测试64转码
                //mPicPath = FaceBase64Util.imageToBase64(data.getStringExtra(CameraConfig.IMAGE_PATH));

            }
            if (mPicPath != null) {
                mPhotoUri = Uri.fromFile(new File(mPicPath));
                tvPath.setText(mPicPath);
                GlideImageUtils.displayImageFitCenter(this, ivPicture, mPicPath, R.mipmap.wel1);
            }

        }
    }

    public String convertUriToFilePath(Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            ContentResolver cr = getContentResolver();
            if (uri.getScheme().equals("content")) {//判断uri地址是以什么开头的
                cursor = cr.query(uri, projection, null, null, null);
            } else {//getFileUri判断地址如果以file开头
                cursor = cr.query(FileUrlUtils.getFileUri(this, uri), null, null, null, null);
            }
            cursor.moveToFirst();
            int imageIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(imageIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void takePhoto() {
        Intent intent = new Intent(this, CropActivity.class);
//        intent.putExtra(CameraConfig.RATIO_WIDTH, 855);
//        intent.putExtra(CameraConfig.RATIO_HEIGHT, 541);
//        intent.putExtra(CameraConfig.RATIO_WIDTH, 4);
//        intent.putExtra(CameraConfig.RATIO_HEIGHT, 3);
//        intent.putExtra(CameraConfig.PERCENT_LARGE, 0.8f);
        intent.putExtra(CameraConfig.MASK_COLOR, 0x99000000);
        intent.putExtra(CameraConfig.TOP_OFFSET, 0);
        intent.putExtra(CameraConfig.RECT_CORNER_COLOR, 0xff00ff00);
        intent.putExtra(CameraConfig.TEXT_COLOR, 0xffffffff);
        intent.putExtra(CameraConfig.HINT_TEXT, "");
        intent.putExtra(CameraConfig.IMAGE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/CameraCardCrop/" + System.currentTimeMillis() + ".jpg");
        startActivityForResult(intent, CODE_CROP_PIC);
    }

}
