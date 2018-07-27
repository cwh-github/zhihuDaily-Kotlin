package com.example.cwh_pc.dailynewsstudy.network

import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.view.MenuViewItem
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

//单例模式创建Retrofit实例

class RetrofitNetWork {

    companion object {
        const val TIME_OUT = 20L
        private val BASE_URL = "http://news-at.zhihu.com/api/4/"
        private var mRetrofitInstance: Retrofit? = null

        fun newRetrofitInstance(): Retrofit {
            if (mRetrofitInstance == null) {
                val cilent = OkHttpClient.Builder()
                        //add log Interceptor
                        .addInterceptor {
                            val request = it.request()
                            if (LogUtils.isDebug) {
                                val requestBody = request.body()
                                val hasrequestBody = requestBody != null
                                if (hasrequestBody) {
                                    requestBody!!.let {
                                        val mediaType = it.contentType()
                                        val method = request.method()
                                        val url = request.url().toString()
                                        val requestBodyText = it.toString()
                                        LogUtils.d(msg = " Requset is $request \n " +
                                                "Method is $method   MediaType is $mediaType  " +
                                                "url is $url \n " +
                                                "requestBody is $requestBodyText")
                                    }
                                    val response = it.proceed(request)
                                    val mediaType=response.body()?.contentType()
                                    val bytes=response.body()?.bytes()

                                    if(bytes!=null){
                                        val responseContent= String(bytes!!, Charset.defaultCharset())
                                        LogUtils.d(msg="response header:  ${response.headers()}\n" +
                                                " ------ response body: $responseContent")
                                    }else{
                                        LogUtils.d(msg="response header:  ${response.headers()}\n" +
                                                " ------ response body is Null")
                                    }
                                    response.newBuilder()
                                            .body(ResponseBody.create(mediaType, bytes))
                                            .build()
                                }else{
                                    LogUtils.d(msg="Request Body is Null")
                                    LogUtils.d(msg="Request url is ${request.url()}")
                                    it.proceed(request)
                                }

                            } else {
                                it.proceed(request)
                            }
                        }
                        .retryOnConnectionFailure(true)
                        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                        .build()
                mRetrofitInstance = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(cilent)
                        .build()

            }
            return mRetrofitInstance!!
        }
    }
}