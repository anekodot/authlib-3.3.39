// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpAuthenticationService extends BaseAuthenticationService {
   private static final Logger LOGGER = LoggerFactory.getLogger(HttpAuthenticationService.class);
   private final Proxy proxy;

   protected HttpAuthenticationService(Proxy proxy) {
      Validate.notNull(proxy);
      this.proxy = proxy;
   }

   public Proxy getProxy() {
      return this.proxy;
   }

   protected HttpURLConnection createUrlConnection(URL url) throws IOException {
      Validate.notNull(url);
      LOGGER.debug("Opening connection to " + url);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection(this.proxy);
      connection.setConnectTimeout(15000);
      connection.setReadTimeout(15000);
      connection.setUseCaches(false);
      return connection;
   }

   public String performPostRequest(URL url, String post, String contentType) throws IOException {
      Validate.notNull(url);
      Validate.notNull(post);
      Validate.notNull(contentType);
      HttpURLConnection connection = this.createUrlConnection(url);
      byte[] postAsBytes = post.getBytes(StandardCharsets.UTF_8);
      connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
      connection.setRequestProperty("Content-Length", postAsBytes.length + "");
      connection.setDoOutput(true);
      LOGGER.debug("Writing POST data to " + url + ": " + post);
      OutputStream outputStream = null;

      try {
         outputStream = connection.getOutputStream();
         IOUtils.write(postAsBytes, outputStream);
      } finally {
         IOUtils.closeQuietly(outputStream);
      }

      LOGGER.debug("Reading data from " + url);
      InputStream inputStream = null;

      String var10;
      try {
         inputStream = connection.getInputStream();
         String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
         LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
         LOGGER.debug("Response: " + result);
         return result;
      } catch (IOException var19) {
         IOUtils.closeQuietly(inputStream);
         inputStream = connection.getErrorStream();
         if (inputStream == null) {
            LOGGER.debug("Request failed", var19);
            throw var19;
         }

         LOGGER.debug("Reading error page from " + url);
         String resultx = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
         LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
         LOGGER.debug("Response: " + resultx);
         var10 = resultx;
      } finally {
         IOUtils.closeQuietly(inputStream);
      }

      return var10;
   }

   public String performGetRequest(URL url) throws IOException {
      return this.performGetRequest(url, null);
   }

   public String performGetRequest(URL url, @Nullable String authentication) throws IOException {
      Validate.notNull(url);
      HttpURLConnection connection = this.createUrlConnection(url);
      if (authentication != null) {
         connection.setRequestProperty("Authorization", authentication);
      }

      LOGGER.debug("Reading data from " + url);
      InputStream inputStream = null;

      String var7;
      try {
         inputStream = connection.getInputStream();
         String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
         LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
         LOGGER.debug("Response: " + result);
         return result;
      } catch (IOException var11) {
         IOUtils.closeQuietly(inputStream);
         inputStream = connection.getErrorStream();
         if (inputStream == null) {
            LOGGER.debug("Request failed", var11);
            throw var11;
         }

         LOGGER.debug("Reading error page from " + url);
         String resultx = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
         LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
         LOGGER.debug("Response: " + resultx);
         var7 = resultx;
      } finally {
         IOUtils.closeQuietly(inputStream);
      }

      return var7;
   }

   public static URL constantURL(String url) {
      try {
         return new URL(url);
      } catch (MalformedURLException var2) {
         throw new Error("Couldn't create constant for " + url, var2);
      }
   }

   public static String buildQuery(Map<String, Object> query) {
      if (query == null) {
         return "";
      } else {
         StringBuilder builder = new StringBuilder();

         for (Entry<String, Object> entry : query.entrySet()) {
            if (builder.length() > 0) {
               builder.append('&');
            }

            try {
               builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException var6) {
               LOGGER.error("Unexpected exception building query", var6);
            }

            if (entry.getValue() != null) {
               builder.append('=');

               try {
                  builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
               } catch (UnsupportedEncodingException var5) {
                  LOGGER.error("Unexpected exception building query", var5);
               }
            }
         }

         return builder.toString();
      }
   }

   public static URL concatenateURL(URL url, String query) {
      try {
         return url.getQuery() != null && url.getQuery().length() > 0
            ? new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "&" + query)
            : new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "?" + query);
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", var3);
      }
   }
}
