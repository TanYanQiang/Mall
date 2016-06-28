package com.tgh.devkit.core.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.tgh.devkit.core.R;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.CharBuffer;

/**
 * Created by albert on 16/1/8.
 */
public class IO {

    private IO() {
    }

    private static final int BUF_SIZE = 0x1000; // 4K


    public static File getCacheDir(Context context, String uniqueName) {

        //检测是否有SD卡读写权限
        boolean permission = false;
        DebugLog.i("context EXTERNAL PERMISSION:" + context);
        if (context instanceof Activity) {
            permission = PermissionHelper.checkPermission((Activity) context, context.getResources().getString(R.string.permission_storage), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }
        DebugLog.i("EXTERNAL PERMISSION:" + permission);

        String cachePath;
        if (permission && (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) |
                !Environment.isExternalStorageRemovable())) {
            cachePath = Environment.getExternalStorageDirectory().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File dir = new File(cachePath + File.separator + uniqueName);
        if (!exist(dir)) {
            dir.mkdirs();
        }
        return dir;
    }

    public static long copy(InputStream from, OutputStream to)
            throws IOException {
        if (from == null || to == null) {
            return 0;
        }
        byte[] buf = new byte[BUF_SIZE];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    public static long copy(Readable from, Appendable to)
            throws IOException {
        if (from == null || to == null) {
            return 0;
        }

        CharBuffer buf = CharBuffer.allocate(BUF_SIZE);
        long total = 0;
        while (from.read(buf) != -1) {
            buf.flip();
            to.append(buf);
            total += buf.remaining();
            buf.clear();
        }
        return total;
    }

    public static void copy(File src, File dest) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            copy(in, out);
        } finally {
            silentClose(in);
            silentClose(out);
        }
    }

    public static void copy(URL fromUrl, File destFile) throws IOException {
        if (fromUrl == null || destFile == null) {
            return;
        }
        HttpURLConnection connection = null;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            connection = (HttpURLConnection) fromUrl.openConnection();
            in = fromUrl.openStream();
            out = new FileOutputStream(destFile);
            copy(in, out);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            silentClose(in);
            silentClose(out);
        }
    }

    public static void copy(InputStream in, File destFile) throws IOException {
        if (in == null || destFile == null) {
            return;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(destFile);
            copy(in, out);
        } finally {
            silentClose(in);
            silentClose(out);
        }
    }

    public static byte[] read(InputStream from) throws IOException {
        if (from == null) {
            return null;
        }
        try {
            ByteArrayOutputStream to = new ByteArrayOutputStream();
            copy(from, to);
            return to.toByteArray();
        } finally {
            silentClose(from);
        }
    }

    public static String read(Readable r) throws IOException {
        StringBuilder sb = new StringBuilder();
        copy(r, sb);
        return sb.toString();
    }


    public static byte[] read(File file) throws IOException {
        if (!exist(file)) {
            return null;
        }
        FileInputStream src = null;
        try {
            ByteArrayOutputStream dest = new ByteArrayOutputStream();
            src = new FileInputStream(file);
            copy(src, dest);
            return dest.toByteArray();
        } finally {
            silentClose(src);
        }
    }

    public static byte[] read(File file, int offset, int len) {
        if (!exist(file)) {
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        if (offset < 0) {
            return null;
        }
        if (len <= 0) {
            return null;
        }
        if (offset + len > (int) file.length()) {
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(file, "r");
            b = new byte[len]; // ļС
            in.seek(offset);
            in.readFully(b);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public static byte[] read(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            ByteArrayOutputStream dest = new ByteArrayOutputStream();
            copy(in, dest);
            return dest.toByteArray();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            silentClose(in);
        }
    }

    public static void silentClose(Closeable stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean exist(File file) {
        return file != null && file.exists();
    }


    public static String silentURLEncode(String value, String charset) {
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException ignored) {
            return value;
        }
    }

    public static String silentURLEncode(String value) {
        return silentURLEncode(value, "UTF-8");
    }


    public static String silentURLDecode(String value, String charset) {
        try {
            return URLDecoder.decode(value, charset);
        } catch (UnsupportedEncodingException ignored) {
            return value;
        }
    }

    public static String silentURLDecode(String value) {
        return silentURLDecode(value, "UTF-8");
    }

    public static void delete(String path) {
        delete(new File(path));
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f);
            }
        }
        if (file.exists()) {
            boolean deleteFlag = file.delete();
            if (!deleteFlag) {
                DebugLog.i("delete " + file.getAbsolutePath() + " : " + deleteFlag);
            }
        }
    }
}
