// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.exceptions;

public class MinecraftClientException extends RuntimeException {
   protected final ErrorType type;

   protected MinecraftClientException(ErrorType type, String message) {
      super(message);
      this.type = type;
   }

   public MinecraftClientException(ErrorType type, String message, Throwable cause) {
      super(message, cause);
      this.type = type;
   }

   public ErrorType getType() {
      return this.type;
   }

   public AuthenticationException toAuthenticationException() {
      return new AuthenticationException(this);
   }

   public static enum ErrorType {
      SERVICE_UNAVAILABLE,
      HTTP_ERROR,
      JSON_ERROR;
   }
}
