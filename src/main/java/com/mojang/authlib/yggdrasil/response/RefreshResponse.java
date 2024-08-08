// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.GameProfile;

public class RefreshResponse extends Response {
   private String accessToken;
   private String clientToken;
   private GameProfile selectedProfile;
   private GameProfile[] availableProfiles;
   private User user;

   public String getAccessToken() {
      return this.accessToken;
   }

   public String getClientToken() {
      return this.clientToken;
   }

   public GameProfile[] getAvailableProfiles() {
      return this.availableProfiles;
   }

   public GameProfile getSelectedProfile() {
      return this.selectedProfile;
   }

   public User getUser() {
      return this.user;
   }
}
