// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.response;

import java.util.Map;
import java.util.StringJoiner;

public class ErrorResponse {
   private final String path;
   private final String error;
   private final String errorMessage;
   private final Map<String, Object> details;

   public ErrorResponse(String path, String error, String errorMessage, Map<String, Object> details) {
      this.path = path;
      this.error = error;
      this.errorMessage = errorMessage;
      this.details = details;
   }

   public String getPath() {
      return this.path;
   }

   public String getError() {
      return this.error;
   }

   public Map<String, Object> getDetails() {
      return this.details;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", ErrorResponse.class.getSimpleName() + "[", "]")
         .add("path='" + this.path + "'")
         .add("error='" + this.error + "'")
         .add("details=" + this.details)
         .add("errorMessage='" + this.errorMessage + "'")
         .toString();
   }
}
