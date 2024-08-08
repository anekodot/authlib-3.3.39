// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.minecraft;;

import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MinecraftProfileTexture {
   public static final int PROFILE_TEXTURE_COUNT = Type.values().length;
   private final String url;
   private final Map<String, String> metadata;

   public MinecraftProfileTexture(String url, Map<String, String> metadata) {
      this.url = url;
      this.metadata = metadata;
   }

   public String getUrl() {
      return this.url;
   }

   @Nullable
   public String getMetadata(String key) {
      return this.metadata == null ? null : this.metadata.get(key);
   }

   public String getHash() {
      return FilenameUtils.getBaseName(this.url);
   }

   @Override
   public String toString() {
      return new ToStringBuilder(this).append("url", this.url).append("hash", this.getHash()).toString();
   }

   public static enum Type {
      SKIN,
      CAPE,
      ELYTRA;
   }
}
