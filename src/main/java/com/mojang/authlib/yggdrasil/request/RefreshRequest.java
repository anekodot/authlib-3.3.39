// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.GameProfile;

public class RefreshRequest {
   private String clientToken;
   private String accessToken;
   private GameProfile selectedProfile;
   private boolean requestUser = true;

   public RefreshRequest(String accessToken, String clientToken) {
      this(accessToken, clientToken, null);
   }

   public RefreshRequest(String accessToken, String clientToken, GameProfile profile) {
      this.clientToken = clientToken;
      this.accessToken = accessToken;
      this.selectedProfile = profile;
   }
}
