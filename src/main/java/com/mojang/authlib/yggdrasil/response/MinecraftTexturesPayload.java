// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import java.util.UUID;

public class MinecraftTexturesPayload {
   private long timestamp;
   private UUID profileId;
   private String profileName;
   private boolean isPublic;
   private Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures;

   public long getTimestamp() {
      return this.timestamp;
   }

   public UUID getProfileId() {
      return this.profileId;
   }

   public String getProfileName() {
      return this.profileName;
   }

   public boolean isPublic() {
      return this.isPublic;
   }

   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures() {
      return this.textures;
   }
}
