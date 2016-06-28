package com.yalantis.ucrop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapLoadUtils {
    private static final String TAG = "BitmapLoadUtils";

    public static void decodeBitmapInBackground(Context context, Uri uri, int requiredWidth, int requiredHeight, BitmapLoadCallback loadCallback) {
        new BitmapWorkerTask(context, uri, requiredWidth, requiredHeight, loadCallback).execute(new Void[0]);
    }

    public static Bitmap transformBitmap(Bitmap bitmap, Matrix transformMatrix) {
        try {
            Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), transformMatrix, true);
            if (bitmap != converted) {
                bitmap.recycle();
                bitmap = converted;
            }
        } catch (OutOfMemoryError error) {
            Log.e("BitmapLoadUtils", "transformBitmap: ", error);
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if ((height > reqHeight) || (width > reqWidth)) {
            while ((height / inSampleSize > reqHeight) || (width / inSampleSize > reqWidth)) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static int getExifOrientation(Context context, Uri imageUri) {
        int orientation = 0;
        try {
            InputStream stream = context.getContentResolver().openInputStream(imageUri);
            if (stream == null) {
                return orientation;
            }
            orientation = new ImageHeaderParser(stream).getOrientation();
            close(stream);
        } catch (IOException e) {
            Log.e("BitmapLoadUtils", "getExifOrientation: " + imageUri.toString(), e);
        }
        return orientation;
    }

    private static int exifToDegrees(int exifOrientation) {
        int rotation;
        switch (exifOrientation) {
            case 5:
            case 6:
                rotation = 90;
                break;
            case 3:
            case 4:
                rotation = 180;
                break;
            case 7:
            case 8:
                rotation = 270;
                break;
            default:
                rotation = 0;
        }
        return rotation;
    }

    private static int exifToTranslation(int exifOrientation) {
        int translation;
        switch (exifOrientation) {
            case 2:
            case 4:
            case 5:
            case 7:
                translation = -1;
                break;
            case 3:
            case 6:
            default:
                translation = 1;
        }
        return translation;
    }

    public static void close(Closeable c) {
        if ((c != null) && ((c instanceof Closeable)))
            try {
                c.close();
            } catch (IOException e) {
            }
    }

    static class BitmapWorkerTask extends AsyncTask<Void, Void, BitmapLoadUtils.BitmapWorkerResult> {
        private final Context mContext;
        private final Uri mUri;
        private final int mRequiredWidth;
        private final int mRequiredHeight;
        private final BitmapLoadUtils.BitmapLoadCallback mBitmapLoadCallback;

        public BitmapWorkerTask(Context context, Uri uri, int requiredWidth, int requiredHeight,
                                BitmapLoadUtils.BitmapLoadCallback loadCallback) {
            this.mContext = context;
            this.mUri = uri;
            this.mRequiredWidth = requiredWidth;
            this.mRequiredHeight = requiredHeight;
            this.mBitmapLoadCallback = loadCallback;
        }

        protected BitmapLoadUtils.BitmapWorkerResult doInBackground(Void[] params) {
            if (this.mUri == null) {
                return new BitmapLoadUtils.BitmapWorkerResult(null, new NullPointerException("Uri cannot be null"));
            }
            ParcelFileDescriptor parcelFileDescriptor;
            try {
                parcelFileDescriptor = this.mContext.getContentResolver().openFileDescriptor(this.mUri, "r");
            } catch (FileNotFoundException e) {
                return new BitmapLoadUtils.BitmapWorkerResult(null, e);
            }
            FileDescriptor fileDescriptor;
            if (parcelFileDescriptor != null) {

                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            } else {
                return new BitmapLoadUtils.BitmapWorkerResult(null, new NullPointerException("ParcelFileDescriptor was null for given Uri"));
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            options.inSampleSize = BitmapLoadUtils.calculateInSampleSize(options, this.mRequiredWidth, this.mRequiredHeight);
            options.inJustDecodeBounds = false;

            Bitmap decodeSampledBitmap = null;

            boolean success = false;
            while (!success) {
                try {
                    decodeSampledBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
                    success = true;
                } catch (OutOfMemoryError error) {
                    Log.e("BitmapLoadUtils", "doInBackground: BitmapFactory.decodeFileDescriptor: ", error);
                    options.inSampleSize += 1;
                }
            }

            if (Build.VERSION.SDK_INT >= 16) {
                BitmapLoadUtils.close(parcelFileDescriptor);
            }

            int exifOrientation = BitmapLoadUtils.getExifOrientation(this.mContext, this.mUri);
            int exifDegrees = BitmapLoadUtils.exifToDegrees(exifOrientation);
            int exifTranslation = BitmapLoadUtils.exifToTranslation(exifOrientation);

            Matrix matrix = new Matrix();
            if (exifDegrees != 0) {
                matrix.preRotate(exifDegrees);
            }
            if (exifTranslation != 1) {
                matrix.postScale(exifTranslation, 1.0F);
            }
            if (!matrix.isIdentity()) {
                return new BitmapLoadUtils.BitmapWorkerResult(BitmapLoadUtils.transformBitmap(decodeSampledBitmap, matrix), null);
            }

            return new BitmapLoadUtils.BitmapWorkerResult(decodeSampledBitmap, null);
        }

        protected void onPostExecute(BitmapLoadUtils.BitmapWorkerResult result) {
            if (result.mBitmapWorkerException == null)
                this.mBitmapLoadCallback.onBitmapLoaded(result.mBitmapResult);
            else
                this.mBitmapLoadCallback.onFailure(result.mBitmapWorkerException);
        }
    }

    static class BitmapWorkerResult {
        Bitmap mBitmapResult;
        Exception mBitmapWorkerException;

        public BitmapWorkerResult(Bitmap bitmapResult, Exception bitmapWorkerException) {
            this.mBitmapResult = bitmapResult;
            this.mBitmapWorkerException = bitmapWorkerException;
        }
    }

    public interface BitmapLoadCallback {
        void onBitmapLoaded(Bitmap paramBitmap);

        void onFailure(Exception paramException);
    }
}