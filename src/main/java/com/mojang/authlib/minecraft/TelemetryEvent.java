// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.minecraft;;

public interface TelemetryEvent extends TelemetryPropertyContainer {
   TelemetryEvent EMPTY = new TelemetryEvent() {
      @Override
      public void addProperty(String id, String value) {
      }

      @Override
      public void addProperty(String id, int value) {
      }

      @Override
      public void addProperty(String id, boolean value) {
      }

      @Override
      public void addNullProperty(String id) {
      }

      @Override
      public void send() {
      }
   };

   void send();
}
