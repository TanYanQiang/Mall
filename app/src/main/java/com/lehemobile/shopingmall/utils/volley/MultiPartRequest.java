package com.lehemobile.shopingmall.utils.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * A request for making a Multi Part request
 * Created by albert on 16/1/12.
 */
public abstract class MultiPartRequest<T> extends Request<T> {

    private static final String PROTOCOL_CHARSET = "utf-8";
    private Response.Listener<T> mListener;
    private Map<String, MultiPartParam> mMultipartParams = null;
    private Map<String, String> mFileUploads = null;
    private boolean isFixedStreamingMode;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Request.Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MultiPartRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(method, url, errorListener);
        mListener = listener;
        mMultipartParams = new HashMap<>();
        mFileUploads = new HashMap<>();

    }

    /**
     * Add a parameter to be sent in the multipart request
     *
     * @param name The name of the paramter
     * @param contentType The content type of the paramter
     * @param value the value of the paramter
     * @return The Multipart request for chaining calls
     */
    public MultiPartRequest<T> addMultipartParam(String name, String contentType, String value) {
        mMultipartParams.put(name, new MultiPartParam(contentType, value));
        return this;
    }

    /**
     * Add a file to be uploaded in the multipart request
     *
     * @param name The name of the file key
     * @param filePath The path to the file. This file MUST exist.
     * @return The Multipart request for chaining method calls
     */
    public MultiPartRequest<T> addFile(String name, String filePath) {
        mFileUploads.put(name, filePath);
        return this;
    }

    @Override
    abstract public Response<T> parseNetworkResponse(NetworkResponse response);

    @Override
    protected void deliverResponse(T response) {
        if(null != mListener){
            mListener.onResponse(response);
        }
    }


    /**
     * A representation of a MultiPart parameter
     */
    public static final class MultiPartParam {

        public String contentType;
        public String value;

        /**
         * Initialize a multipart request param with the value and content type
         *
         * @param contentType The content type of the param
         * @param value The value of the param
         */
        public MultiPartParam(String contentType, String value) {
            this.contentType = contentType;
            this.value = value;
        }
    }

    /**
     * Get all the multipart params for this request
     *
     * @return A map of all the multipart params NOT including the file uploads
     */
    public Map<String, MultiPartParam> getMultipartParams() {
        return mMultipartParams;
    }

    /**
     * Get all the files to be uploaded for this request
     *
     * @return A map of all the files to be uploaded for this request
     */
    public Map<String, String> getFilesToUpload() {
        return mFileUploads;
    }

    /**
     * Get the protocol charset
     */
    public String getProtocolCharset() {
        return PROTOCOL_CHARSET;
    }

    public boolean isFixedStreamingMode() {
        return isFixedStreamingMode;
    }

    public void setFixedStreamingMode(boolean isFixedStreamingMode) {
        this.isFixedStreamingMode = isFixedStreamingMode;
    }

    public void setMultipartParams(Map<String, MultiPartParam> multipartParams) {
        this.mMultipartParams = multipartParams;
    }

    public void setFileUploads(Map<String, String> fileUploads) {
        this.mFileUploads = fileUploads;
    }
}
