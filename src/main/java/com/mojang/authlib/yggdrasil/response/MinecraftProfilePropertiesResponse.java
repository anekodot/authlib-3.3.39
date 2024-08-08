// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;

public class MinecraftProfilePropertiesResponse extends Response {
   private UUID id;
   private String name;
   private PropertyMap properties;

   public UUID getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public PropertyMap getProperties() {
      return this.properties;
   }
}
