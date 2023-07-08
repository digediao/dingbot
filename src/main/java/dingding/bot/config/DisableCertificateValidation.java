package dingding.bot.config;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DisableCertificateValidation {

    public static void disableCertificateValidation() throws Exception {
        // 创建一个 TrustManager 来接受所有证书
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    }
                }
        };
        // 初始化 SSLContext
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        // 将默认的 HostnameVerifier 替换为接受所有主机名的实现
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        // 将默认的 SSLContext 替换为接受所有证书的实现
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}

