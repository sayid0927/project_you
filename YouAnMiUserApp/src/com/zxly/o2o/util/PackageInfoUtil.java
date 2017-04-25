package com.zxly.o2o.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

/**
 * 用于判断是当前debug 还是release
 * Created by kenwu on 2015/10/9.
 */
public class PackageInfoUtil {


    /**
     * 缺点：只针对单一应用，无法对所有应用进行签名判断。
     * 检测AndroidMainfest.xml的"android:debuggable"属性
     */
    public static boolean isDebuggable(Context ctx) {
        boolean debuggable = false;
        PackageManager pm = ctx.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        }
        catch(PackageManager.NameNotFoundException e) {
        }

        return debuggable;
    }




    private final static X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US");

    /**
     * 根据google 官网提供的开发文档Signing your application，debug key
     * 包含了以下几个属性值："CN=Android Debug,O=Android,C=US"
     */
    private static boolean signatureIsDebuggable(Context ctx) {
        boolean debuggable = false;
        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;
            for (int i = 0; i < signatures.length; i++) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable)
                    break;
            }

        } catch (PackageManager.NameNotFoundException e) {
        } catch (CertificateException e) {
        }
        return debuggable;
    }



}
