package com.tgh.devkit.pickimage;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.tgh.devkit.core.utils.DebugLog;
import com.tgh.devkit.core.utils.IO;
import com.tgh.devkit.core.utils.PermissionHelper;
import com.tgh.devkit.core.utils.Utils;
import com.tgh.devkit.dialog.DialogBuilder;
import com.tgh.devkit.list.R;
import com.tgh.devkit.view.VerticalSheetView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

/**
 * 选择图像帮助类
 * Created by albert on 16/1/14.
 */
public class PickImageHelper {

    public interface PickImageCallback {
        void onPickImageSuccess(String imageFile);

        void onPickImageError();
    }

    private static final int REQ_CODE_PICK_CAMERA = 9001;
    private static final int REQ_CODE_PICK_GALLERY = 9002;
    private static final int REQ_CODE_CROP_IMAGE = 9003;

    private Activity activity;
    private Fragment fragment;
    private Dialog pickImageDialog;
    private File tempFile;

    private boolean crop = true;
    private float aspectRatioX = 1;
    private float aspectRatioY = 1;
    private int maxResultWidth;
    private int maxResultHeight;

    private PickImageCallback pickImageCallback;

    public PickImageHelper(Activity activity) {
        this.activity = activity;
        init();
    }

    public PickImageHelper(Fragment fragment) {
        this.fragment = fragment;
        init();
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public void setPickImageCallback(PickImageCallback pickImageCallback) {
        this.pickImageCallback = pickImageCallback;
    }

    public void setAspectRatio(float x, float y) {
        this.aspectRatioX = x;
        this.aspectRatioY = y;
    }

    public void setMaxResultSize(int width, int height) {
        this.maxResultWidth = width;
        this.maxResultHeight = height;
    }

    private void init() {
        File cacheDir = IO.getCacheDir(getActivity(), "image");
        tempFile = new File(cacheDir, "PickImageHelper.jpg");
        if (IO.exist(tempFile)) {
            tempFile.delete();
        }
        maxResultWidth = 800;
        maxResultHeight = 800;
    }

    private Activity getActivity() {
        return fragment == null ? activity : fragment.getActivity();
    }

    private void generateDialog() {
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.v_pick_image,
                null, false);

        VerticalSheetView vs1  = (VerticalSheetView) contentView.findViewById(R.id.vs1);
        vs1.addLines(getActivity().getResources().getStringArray(R.array.pick_image));
        vs1.setItemClickListener(new VerticalSheetView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                if (position == 0){
                    pickImageDialog.dismiss();
                    pickFromCamera();
                }else {
                    pickImageDialog.dismiss();
                    pickFromGallery();
                }
            }
        });

        VerticalSheetView vs2  = (VerticalSheetView) contentView.findViewById(R.id.vs2);
        vs2.setItemClickListener(new VerticalSheetView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                pickImageDialog.dismiss();
            }
        });

        pickImageDialog = new DialogBuilder(getActivity())
                .contentView(contentView)
                .build();

    }


    public void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.CONTENT_TYPE
        );

        if (intent.resolveActivity(getActivity().getPackageManager()) == null) {
            Toast.makeText(getActivity(), R.string.no_gallery_app, Toast.LENGTH_SHORT).show();
            return;
        }

        startActivityForResult(intent, REQ_CODE_PICK_GALLERY);
    }

    public void pickFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        intent.putExtra("return-data", true);

        if (intent.resolveActivity(getActivity().getPackageManager()) == null) {
            Toast.makeText(getActivity(), R.string.no_camera_app, Toast.LENGTH_SHORT).show();
            return;
        }

        startActivityForResult(intent, REQ_CODE_PICK_CAMERA);
    }

    private void startActivityForResult(Intent intent, int reqCode) {
        if (fragment != null) {
            fragment.startActivityForResult(intent, reqCode);
        } else {
            activity.startActivityForResult(intent, reqCode);
        }
    }


    public void showPickDialog() {
        if (pickImageDialog == null) {
            generateDialog();
        }
        if (!checkPermission()) return;
        pickImageDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case REQ_CODE_PICK_CAMERA:
                if (resultCode != Activity.RESULT_OK) {
                    onError(R.string.choose_image_error);
                    return;
                }
                onPickSuccess();
                break;
            case REQ_CODE_PICK_GALLERY:
                if (resultCode != Activity.RESULT_OK) {
                    onError(R.string.choose_image_error);
                    return;
                }
                try {
                    Uri uri = data.getData();
                    IO.copy(getActivity().getContentResolver().openInputStream(uri), tempFile);
                    onPickSuccess();
                } catch (IOException e) {
                    onError(R.string.choose_image_error);
                    DebugLog.e(e.getMessage(), e);
                }
                break;
            case REQ_CODE_CROP_IMAGE:
                if (resultCode != Activity.RESULT_OK) {
                    onError(R.string.choose_image_error);
                    return;
                }

                Uri uri = UCrop.getOutput(data);
                if (uri == null) {
                    onError(R.string.choose_image_error);
                    return;
                }

                if (null != pickImageCallback) {
                    pickImageCallback.onPickImageSuccess(uri.getPath());
                }
                break;

        }

    }

    private void onPickSuccess() {
        if (!crop) {
            if (null != pickImageCallback) {
                pickImageCallback.onPickImageSuccess(tempFile.getPath());
            }
            return;
        }


        UCrop uCrop = UCrop.of(Uri.fromFile(tempFile), Uri.fromFile(tempFile))
                .withMaxResultSize(maxResultWidth, maxResultHeight);
        if (aspectRatioX != 0 && aspectRatioY != 0) {
            uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        }
        uCrop.start(getActivity(), REQ_CODE_CROP_IMAGE);
    }

    private void onError(@StringRes int message) {
        if (null != pickImageCallback) {
            pickImageCallback.onPickImageError();
        }
    }

    private boolean checkPermission() {
        return PermissionHelper.checkPermission(getActivity(), PermissionHelper.PERMISSION_STORAGE,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }
}
