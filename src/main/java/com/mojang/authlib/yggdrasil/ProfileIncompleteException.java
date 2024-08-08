// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil;

public class ProfileIncompleteException extends RuntimeException {
   public ProfileIncompleteException() {
   }

   public ProfileIncompleteException(String message) {
      super(message);
   }

   public ProfileIncompleteException(String message, Throwable cause) {
      super(message, cause);
   }

   public ProfileIncompleteException(Throwable cause) {
      super(cause);
   }
}
