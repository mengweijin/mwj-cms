package com.mwj.cms.common.util.http;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author Meng Wei Jin
 * @description 信任所有SSL证书
 **/
public class HttpsTrustAnyManager {

    /**
     * 信任所有SSL证书
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLSocketFactory getTrustAnySSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{new HttpsTrustAnyManager.TrustAnyTrustManager()}, new java.security.SecureRandom());
        return sc.getSocketFactory();
    }

    /**
     * 信任所有主机名验证
     * @return
     */
    public static TrustAnyHostnameVerifier getTrustAnyHostnameVerifier() {
        return new HttpsTrustAnyManager.TrustAnyHostnameVerifier();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
