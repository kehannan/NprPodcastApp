package com.hannan.kevin;

import okhttp3.logging.HttpLoggingInterceptor;


import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Platform;
import okhttp3.internal.http.HttpEngine;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.Platform.INFO;

/**
 * Created by kehannan on 6/7/16.
 */
public class LoggingInterceptor {

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


}
