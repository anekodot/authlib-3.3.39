// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib;

public class Agent {
   public static final Agent MINECRAFT = new Agent("Minecraft", 1);
   public static final Agent SCROLLS = new Agent("Scrolls", 1);
   private final String name;
   private final int version;

   public Agent(String name, int version) {
      this.name = name;
      this.version = version;
   }

   public String getName() {
      return this.name;
   }

   public int getVersion() {
      return this.version;
   }

   @Override
   public String toString() {
      return "Agent{name='" + this.name + "', version=" + this.version + "}";
   }
}
