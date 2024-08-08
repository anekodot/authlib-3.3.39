// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.exceptions;

public class InvalidCredentialsException extends AuthenticationException {
   public InvalidCredentialsException() {
   }

   public InvalidCredentialsException(String message) {
      super(message);
   }

   public InvalidCredentialsException(String message, Throwable cause) {
      super(message, cause);
   }

   public InvalidCredentialsException(Throwable cause) {
      super(cause);
   }
}
