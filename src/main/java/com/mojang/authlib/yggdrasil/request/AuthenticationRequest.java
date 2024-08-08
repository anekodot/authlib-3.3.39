// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.Agent;

public class AuthenticationRequest {
   private Agent agent;
   private String username;
   private String password;
   private String clientToken;
   private boolean requestUser = true;

   public AuthenticationRequest(Agent agent, String username, String password, String clientToken) {
      this.agent = agent;
      this.username = username;
      this.password = password;
      this.clientToken = clientToken;
   }
}
