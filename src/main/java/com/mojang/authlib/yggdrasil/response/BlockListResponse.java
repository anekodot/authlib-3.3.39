// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.response;

import java.util.Set;
import java.util.UUID;

public class BlockListResponse extends Response {
   private Set<UUID> blockedProfiles;

   public Set<UUID> getBlockedProfiles() {
      return this.blockedProfiles;
   }
}