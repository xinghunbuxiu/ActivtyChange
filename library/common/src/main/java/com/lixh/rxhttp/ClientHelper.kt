package com.lixh.rxhttp

import android.content.Context
import com.lixh.app.BaseApplication
import com.lixh.utils.UNetWork
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.math.BigInteger
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import javax.net.ssl.*

object ClientHelper {
    @Volatile
    private var serverTime: Long = 0


    //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
    //有网络情况下，根据请求接口的设置
    //服务端设置了缓存策略，则使用服务端缓存策略
    //服务端未设置缓存策略，则客户端自行设置
    //无网络
    //拦截器
    class autoCacheInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            if (!UNetWork.isNetworkAvailable(BaseApplication.appContext)) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }

            val response = chain.proceed(request)
            if (!UNetWork.isNetworkAvailable(BaseApplication.appContext)) {
                val cacheControl = request.cacheControl.toString()
                cacheControl.let {
                    response.newBuilder().header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build()
                }

            } else {
                response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=3600000")
                        .removeHeader("Pragma")
                        .build()
            }
            return response
        }
    }

    val httpLoggingInterceptor: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            return loggingInterceptor
        }


    //对于预置证书来说，使用这种方式可以。如果对于使用自签名正来说，使用以下代码无形之中给
    //给攻击者留下了一道门。
    //客户端中复写android默认的证书检查机制（X509TrustManager）,并且在代码中无任何校验ssl证书有效的代码
    // Create a trust manager that does not validate certificate chains
    //客户端并为对ssl证书的有效性进行校验，并切使用了自定义方法覆盖android自带的校验方法
    // Perform customary SSL/TLS checks
    // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
    // with 0×30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0×00 to drop.
    /* positive */// Pin it!
    // Install the all-trusting trust manager
    // Create an ssl socket factory with our all-trusting manager
    val sslSocketFactory: SSLSocketFactory
        @Throws(Exception::class)
        get() {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                val PUB_KEY = "3082010a0282010100d52ff5dd432b3a05113ec1a7065fa5a80308810e4e181cf14f7598c8d553cccb7d5111fdcdb55f6ee84fc92cd594adc1245a9c4cd41cbe407a919c5b4d4a37a012f8834df8cfe947c490464602fc05c18960374198336ba1c2e56d2e984bdfb8683610520e417a1a9a5053a10457355cf45878612f04bb134e3d670cf96c6e598fd0c693308fe3d084a0a91692bbd9722f05852f507d910b782db4ab13a92a7df814ee4304dccdad1b766bb671b6f8de578b7f27e76a2000d8d9e6b429d4fef8ffaa4e8037e167a2ce48752f1435f08923ed7e2dafef52ff30fef9ab66fdb556a82b257443ba30a93fda7a0af20418aa0b45403a2f829ea6e4b8ddbb9987f1bf0203010001"

                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                        chain: Array<java.security.cert.X509Certificate>,
                        authType: String) {


                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                        chain: Array<java.security.cert.X509Certificate>?,
                        authType: String?) {
                    if (chain == null) {
                        throw IllegalArgumentException("checkServerTrusted:x509Certificate array isnull")
                    }

                    if (chain.size <= 0) {
                        throw IllegalArgumentException("checkServerTrusted: X509Certificate is empty")
                    }

                    if (!(null != authType && authType.equals("RSA", ignoreCase = true))) {
                        throw CertificateException("checkServerTrusted: AuthType is not RSA")
                    }
                    try {
                        val tmf = TrustManagerFactory.getInstance("X509")
                        tmf.init(null as KeyStore?)
                        for (trustManager in tmf.trustManagers) {
                            (trustManager as X509TrustManager).checkServerTrusted(chain, authType)
                        }
                    } catch (e: Exception) {
                        throw CertificateException(e)
                    }

                    val pubkey = chain[0].publicKey as RSAPublicKey

                    val encoded = BigInteger(1, pubkey.encoded).toString(16)
                    val expected = PUB_KEY.equals(encoded, ignoreCase = true)

                    if (!expected) {
                        throw CertificateException("checkServerTrusted: Expected public key: "
                                + PUB_KEY + ", got public key:" + encoded)
                    }

                }


                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts,
                    java.security.SecureRandom())
            return sslContext
                    .socketFactory
        }


    internal fun getSSLSocketFactory(context: Context?, certificates: IntArray): SSLSocketFactory? {

        if (context == null) {
            throw NullPointerException("context == null")
        }

        //CertificateFactory用来证书生成
        val certificateFactory: CertificateFactory
        try {
            certificateFactory = CertificateFactory.getInstance("X.509")
            //Create a KeyStore containing our trusted CAs
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)

            for (i in certificates.indices) {
                //读取证书文件
                val `is` = context.resources.openRawResource(certificates[i])
                keyStore.setCertificateEntry(i.toString(), certificateFactory.generateCertificate(`is`))

                `is`?.close()
            }
            //Create a TrustManager that trusts the CAs in our keyStore
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            //Create an SSLContext that uses our TrustManager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())
            return sslContext.socketFactory

        } catch (e: Exception) {

        }

        return null
    }

    internal fun getHostnameVerifier(hostUrls: Array<String>): HostnameVerifier {

        return HostnameVerifier { hostname, session ->
            var ret = false
            for (host in hostUrls) {
                if (host.equals(hostname, ignoreCase = true)) {
                    ret = true
                }
            }
            ret
        }


    }


}