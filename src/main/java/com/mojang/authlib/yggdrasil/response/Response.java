// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.response;

public class Response {
   private String error;
   private String errorMessage;
   private String cause;

   public String getError() {
      return this.error;
   }

   public String getCause() {
      return this.cause;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   protected void setError(String error) {
      this.error = error;
   }

   protected void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
   }

   protected void setCause(String cause) {
      this.cause = cause;
   }
}
