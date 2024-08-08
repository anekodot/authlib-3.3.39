// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
   LEGACY("legacy"),
   MOJANG("mojang");

   private static final Map<String, UserType> BY_NAME = new HashMap<>();
   private final String name;

   private UserType(String name) {
      this.name = name;
   }

   public static UserType byName(String name) {
      return BY_NAME.get(name.toLowerCase());
   }

   public String getName() {
      return this.name;
   }

   static {
      for (UserType type : values()) {
         BY_NAME.put(type.name, type);
      }
   }
}
