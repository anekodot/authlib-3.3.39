// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.minecraft;;

import com.mojang.authlib.AuthenticationService;

public abstract class BaseMinecraftSessionService implements MinecraftSessionService {
   private final AuthenticationService authenticationService;

   protected BaseMinecraftSessionService(AuthenticationService authenticationService) {
      this.authenticationService = authenticationService;
   }

   public AuthenticationService getAuthenticationService() {
      return this.authenticationService;
   }
}
