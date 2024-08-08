// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.request;

public class ValidateRequest {
   private String clientToken;
   private String accessToken;

   public ValidateRequest(String accessToken, String clientToken) {
      this.clientToken = clientToken;
      this.accessToken = accessToken;
   }
}
