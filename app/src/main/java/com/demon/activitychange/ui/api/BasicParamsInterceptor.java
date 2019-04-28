package com.demon.activitychange.ui.api;

import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


/**
 * Created by jk.yeo on 16/3/4 15:28.
 * Mail to ykooze@gmail.com
 */
public class BasicParamsInterceptor implements Interceptor {
    Map<String, String> queryParamsMap = new HashMap<>();
    Map<String, String> paramsMap = new HashMap<>();
    Map<String, String> headerParamsMap = new HashMap<>();
    List<String> headerLinesList = new ArrayList<>();
    private String userAgent = "Android";
    IRequestIntercept requestIntercept;

    interface IRequestIntercept {
        Request intercept(Request request);
    }

    public void setRequestIntercept(IRequestIntercept requestIntercept) {
        this.requestIntercept = requestIntercept;
    }

    private BasicParamsInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (requestIntercept != null) {
            request = requestIntercept.intercept(request);
        }
        Request.Builder requestBuilder = request.newBuilder();
        Headers.Builder headerBuilder = request.headers().newBuilder();
        if (this.headerParamsMap.size() > 0) {
            Iterator iterator = this.headerParamsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (this.headerLinesList.size() > 0) {
            for (String line : this.headerLinesList) {
                headerBuilder.add(line);
            }
        }
        if (this.queryParamsMap.size() > 0) {
            if (request.method().equals("GET")) {
                request = this.injectParamsIntoUrl(request.url().newBuilder(), requestBuilder, this.queryParamsMap);
            }
        }
        // process post body inject
        if (this.paramsMap.size() > 0) {
            if (this.canInjectIntoBody(request)) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Entry<String, String> entry : this.paramsMap.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue());
                }

                FormBody formBody = formBodyBuilder.build();
                String postBodyString = bodyToString(request.body());
                postBodyString += (postBodyString.length() > 0 ? "&" : "") + bodyToString(formBody);
                requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
            }
        }
        requestBuilder.headers(headerBuilder.build());
        request = requestBuilder.build();
        return chain.proceed(request);
    }

    static String pathSegmentsToString(List<String> pathSegments) {
        StringBuilder out = new StringBuilder();
        for (int i = 0, size = pathSegments.size(); i < size; i++) {
            out.append('/');
            out.append(pathSegments.get(i));
        }
        return out.toString();
    }

    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        return TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded");
    }

    // func to inject params into url
    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }

        return null;
    }

    private static String bodyToString(RequestBody request) {
        try {
            RequestBody copy = request;
            Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (IOException e) {
            return "did not work";
        }
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public static class Builder {
        BasicParamsInterceptor interceptor;

        public Builder() {
            this.interceptor = new BasicParamsInterceptor();
        }

        public void setRequestIntercept(IRequestIntercept intercept) {
            this.interceptor.setRequestIntercept(intercept);
        }

        public void setUserAgent(String userAgent) {
            interceptor.setUserAgent(userAgent);
        }

        public Builder addParam(String key, String value) {
            this.interceptor.paramsMap.put(key, value);
            return this;
        }

        public Builder addParamsMap(Map<String, String> paramsMap) {
            this.interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public Builder addHeaderParam(String key, String value) {
            this.interceptor.headerParamsMap.put(key, value);
            return this;
        }

        public Builder addHeaderParamsMap(Map<String, String> headerParamsMap) {
            this.interceptor.headerParamsMap.putAll(headerParamsMap);
            return this;
        }

        public Builder addHeaderLine(String headerLine) {
            int index = headerLine.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            }
            this.interceptor.headerLinesList.add(headerLine);
            return this;
        }

        public Builder addHeaderLinesList(List<String> headerLinesList) {
            for (String headerLine : headerLinesList) {
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                this.interceptor.headerLinesList.add(headerLine);
            }
            return this;
        }

        public Builder addQueryParam(String key, String value) {
            this.interceptor.queryParamsMap.put(key, value);
            return this;
        }

        public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
            this.interceptor.queryParamsMap.putAll(queryParamsMap);
            return this;
        }

        public BasicParamsInterceptor build() {
            return this.interceptor;
        }

        public Builder addIntercept(IRequestIntercept intercept) {
            interceptor.setRequestIntercept(intercept);
            return this;
        }
    }
}