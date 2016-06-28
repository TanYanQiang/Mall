package com.tgh.devkit.core.cache;

import com.tgh.devkit.core.utils.DebugLog;
import com.tgh.devkit.core.utils.IO;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 针对DiskLruCache的包装类，提供了两个常用的方法 put 和 get
 * Created by albert on 15/12/30.
 */
public class DiskLruCacheWrapper {

    protected final DiskLruCache diskLruCache;

    public DiskLruCacheWrapper(DiskLruCache diskLruCache) {
        this.diskLruCache = diskLruCache;
    }

    public byte[] get(String key){
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot!=null){
                InputStream in = snapshot.getInputStream(0);
                return IO.read(in);
            }
        } catch (IOException e) {
            DebugLog.e(e.getMessage(), e);
        }
        return null;
    }

    public File getFile(String key){
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot!=null){
                return snapshot.getFile(0);
            }
        } catch (IOException e) {
            DebugLog.e(e.getMessage(), e);
        }
        return null;
    }

    public void put(String key, byte[] data) {
        DiskLruCache.Editor edit = null;
        try {
            edit = diskLruCache.edit(key);
            OutputStream outputStream = edit.newOutputStream(0);
            outputStream.write(data);
            outputStream.close();
            edit.commit();
        } catch (IOException e) {
            DebugLog.e( e.getMessage(),e);
            silentAbort(edit);
        }finally {
            silentFlush(diskLruCache);
        }
    }

    public void remove(String key) {
        try {
            diskLruCache.remove(key);
        } catch (IOException e) {
            DebugLog.e( e.getMessage(),e);
        }finally {
            silentFlush(diskLruCache);
        }
    }

    public void clear(){
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            DebugLog.e( e.getMessage(),e);
        }
    }

    protected void silentAbort(DiskLruCache.Editor edit ){
        if (edit!=null){
            try {
                edit.abort();
            } catch (IOException ignored) {
            }
        }
    }

    protected void silentFlush(DiskLruCache diskLruCache){
        if (diskLruCache!=null){
            try {
                diskLruCache.flush();
            } catch (IOException ignored) {
            }
        }
    }
}
