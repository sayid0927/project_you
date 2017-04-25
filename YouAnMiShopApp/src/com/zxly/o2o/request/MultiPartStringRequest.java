/**
 * Copyright 2013 Mani Selvaraj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxly.o2o.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * MultipartRequest - To handle the large file uploads. Extended from
 * JSONRequest. You might want to change to StringRequest based on your response
 * type.
 * 
 * @author Mani Selvaraj
 * 
 */
public class MultiPartStringRequest extends Request<String> implements MultiPartRequest {

	private final Listener<String> mListener;

	private String token;
	/* To hold the parameter name and the File to upload */
	private Map<String, File> fileUploads = new HashMap<String, File>();

	/* To hold the parameter name and the string content to upload */
	private Map<String, Object> stringUploads = new HashMap<String, Object>();

	/**
	 * Creates a new request with the given method.
	 * 
	 * @param method
	 *            the request {@link Method} to use
	 * @param url
	 *            URL to fetch the string at
	 * @param listener
	 *            Listener to receive the String response
	 * @param errorListener
	 *            Error listener, or null to ignore errors
	 */
	public MultiPartStringRequest(int method, String url, Listener<String> listener, ErrorListener errorListener,String token) {
		super(method, url, errorListener);
		mListener = listener;
		this.token = token;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> head = new HashMap<String, String>();
			head.put("Authorization", token);

        head.put("SerialNO", System.currentTimeMillis()+"");
        head.put("DeviceID", AppController.imei);
        head.put("DeviceType","1"); // 1=android;2=iso;3=pc
        return head;
	}

	public void addFileUpload(String param, File file) {
		fileUploads.put(param, file);
	}

	public void addStringUpload(String param, String content) {
		stringUploads.put(param, content);
	}

	public Map<String, File> getFileUploads() {
		return fileUploads;
	}

	public Map<String, Object> getStringUploads() {
		return stringUploads;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(String response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}

	public String getBodyContentType() {

		return null;
	}
}