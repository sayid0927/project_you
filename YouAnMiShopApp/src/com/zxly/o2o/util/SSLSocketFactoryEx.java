package com.zxly.o2o.util;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by fengrongjian on 2016/12/3.
 */
public class SSLSocketFactoryEx extends SSLSocketFactory {
    private static final String TAG = SSLSocketFactoryEx.class.getName();
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        this.sslContext.init((KeyManager[])null, new TrustManager[]{new KeyStoresTrustManagerEX(new KeyStore[]{truststore})}, (SecureRandom)null);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket() throws IOException {
        return this.sslContext.getSocketFactory().createSocket();
    }

    public static class KeyStoresTrustManagerEX implements X509TrustManager {
        protected ArrayList<X509TrustManager> x509TrustManagers = new ArrayList();

        protected KeyStoresTrustManagerEX(KeyStore... additionalkeyStores) {
            ArrayList factories = new ArrayList();

            TrustManagerFactory tmf;
            int var6;
            try {
                tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init((KeyStore)null);
                factories.add(tmf);
                KeyStore[] var7 = additionalkeyStores;
                var6 = additionalkeyStores.length;

                for(int tm = 0; tm < var6; ++tm) {
                    KeyStore keyStore = var7[tm];
                    TrustManagerFactory additionalCerts = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    additionalCerts.init(keyStore);
                    factories.add(additionalCerts);
                }
            } catch (Exception var9) {
                throw new RuntimeException(var9);
            }

            Iterator var10 = factories.iterator();

            while(var10.hasNext()) {
                tmf = (TrustManagerFactory)var10.next();
                TrustManager[] var13;
                int var12 = (var13 = tmf.getTrustManagers()).length;

                for(var6 = 0; var6 < var12; ++var6) {
                    TrustManager var11 = var13[var6];
                    if(var11 instanceof X509TrustManager) {
                        this.x509TrustManagers.add((X509TrustManager)var11);
                    }
                }
            }

            if(this.x509TrustManagers.size() == 0) {
                throw new RuntimeException("Couldn\'t find any X509TrustManagers");
            }
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            X509TrustManager defaultX509TrustManager = (X509TrustManager)this.x509TrustManagers.get(0);
            defaultX509TrustManager.checkClientTrusted(chain, authType);
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Iterator var4 = this.x509TrustManagers.iterator();

            while(var4.hasNext()) {
                X509TrustManager tm = (X509TrustManager)var4.next();

                try {
                    tm.checkServerTrusted(chain, authType);
                    return;
                } catch (CertificateException var6) {
                    ;
                }
            }

            throw new CertificateException();
        }

        public X509Certificate[] getAcceptedIssuers() {
            ArrayList list = new ArrayList();
            Iterator var3 = this.x509TrustManagers.iterator();

            while(var3.hasNext()) {
                X509TrustManager tm = (X509TrustManager)var3.next();
                list.addAll(Arrays.asList(tm.getAcceptedIssuers()));
            }

            return (X509Certificate[])list.toArray(new X509Certificate[list.size()]);
        }
    }
}
