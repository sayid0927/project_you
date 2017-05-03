package com.zxly.o2o.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HurlStack;
import com.easemob.easeui.model.StringBody;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.SSLSocketFactoryEx;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;

import internal.org.apache.http.entity.mime.HttpMultipartMode;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;


/**
 * @author ZhiCheng Guo
 * @version 2014年10月7日 上午11:00:52 这个Stack用于上传文件, 如果没有这个Stack, 则上传文件不成功
 */
public class MultiPartStack extends HurlStack {
    @SuppressWarnings("unused")
    private static final String TAG = MultiPartStack.class.getSimpleName();
    private final static String HEADER_CONTENT_TYPE = "Content-Type";
    private Context context;

    public MultiPartStack(Context context) {
        this.context = context;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {

        if (!(request instanceof MultiPartRequest)) {
            return super.performRequest(request, additionalHeaders);
        } else {
            return performMultiPartRequest(request, additionalHeaders);
        }
    }

    private static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            httpRequest.setHeader(key, headers.get(key));
        }
    }

    public HttpResponse performMultiPartRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        HttpUriRequest httpRequest = createMultiPartRequest(request, additionalHeaders);
        addHeaders(httpRequest, additionalHeaders);
        addHeaders(httpRequest, request.getHeaders());
        HttpParams httpParams = httpRequest.getParams();
        int timeoutMs = request.getTimeoutMs();

        if (timeoutMs != -1) {
            HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
        }

		/* Make a thread safe connection manager for the client */
        HttpClient httpClient = getNewHttpClient(httpParams);
        return httpClient.execute(httpRequest);
    }

    private HttpClient getNewHttpClient(HttpParams httpParams) {
        try {
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);

            KeyStore trustStore= buildKeyStore(context, R.raw.sis_server);
            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

//            HttpParams params = new BasicHttpParams();
//            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, registry);
            return new DefaultHttpClient(ccm, httpParams);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private static KeyStore buildKeyStore(Context context, int certRawResId)
            throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        Certificate cert = readCert(context, certRawResId);
        keyStore.setCertificateEntry("ca", cert);

        return keyStore;
    }

    private static Certificate readCert(Context context, int certResourceID) {
        InputStream inputStream = context.getResources().openRawResource(
                certResourceID);
        Certificate ca = null;

        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(inputStream);

        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return ca;
    }

    static HttpUriRequest createMultiPartRequest(Request<?> request, Map<String, String> additionalHeaders) throws AuthFailureError {
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST: {
                // This is the deprecated way that needs to be handled for backwards
                // compatibility.
                // If the request's post body is null, then the assumption is that
                // the request is
                // GET. Otherwise, it is assumed that the request is a POST.
                byte[] postBody = request.getBody();
                if (postBody != null) {
                    HttpPost postRequest = new HttpPost(request.getUrl());
                    if (request.getBodyContentType() != null)
                        postRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                    HttpEntity entity;
                    entity = new ByteArrayEntity(postBody);
                    postRequest.setEntity(entity);
                    return postRequest;
                } else {
                    return new HttpGet(request.getUrl());
                }
            }
            case Method.GET:
                return new HttpGet(request.getUrl());
            case Method.DELETE:
                return new HttpDelete(request.getUrl());
            case Method.POST: {
                HttpPost postRequest = new HttpPost(request.getUrl());
                if (request.getBodyContentType() != null) {
                    postRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                }
                setMultiPartBody(postRequest, request);
                return postRequest;
            }
            case Method.PUT: {
                HttpPut putRequest = new HttpPut(request.getUrl());
                if (request.getBodyContentType() != null)
                    putRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                setMultiPartBody(putRequest, request);
                return putRequest;
            }
            // Added in source code of Volley libray.
            case Method.PATCH: {
                HttpPatch patchRequest = new HttpPatch(request.getUrl());
                if (request.getBodyContentType() != null)
                    patchRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                return patchRequest;
            }
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }

    /**
     * If Request is MultiPartRequest type, then set MultipartEntity in the
     * httpRequest object.
     *
     * @param httpRequest
     * @param request
     * @throws AuthFailureError
     */
    private static void setMultiPartBody(HttpEntityEnclosingRequestBase httpRequest, Request<?> request) throws AuthFailureError {

        // Return if Request is not MultiPartRequest
        if (!(request instanceof MultiPartRequest)) {
            return;
        }

        MultipartEntity builder = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, null);

        // Iterate the fileUploads
        Map<String, File> fileUpload = ((MultiPartRequest) request).getFileUploads();
        if (fileUpload != null) {
            for (Map.Entry<String, File> entry : fileUpload.entrySet()) {

                builder.addPart(entry.getKey(), new FileBody(entry.getValue()));
            }
        }

        // Iterate the stringUploads
        Map<String, Object> stringUpload = ((MultiPartRequest) request).getStringUploads();
        for (Map.Entry<String, Object> entry : stringUpload.entrySet()) {
            try {
                builder.addPart(entry.getKey(), new StringBody((String) entry.getValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        httpRequest.setEntity(builder);
    }

}
