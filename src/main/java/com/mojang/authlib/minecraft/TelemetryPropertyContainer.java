// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.minecraft;;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public interface TelemetryPropertyContainer {
   void addProperty(String var1, String var2);

   void addProperty(String var1, int var2);

   void addProperty(String var1, boolean var2);

   void addNullProperty(String var1);

   static TelemetryPropertyContainer forJsonObject(final JsonObject object) {
      return new TelemetryPropertyContainer() {
         @Override
         public void addProperty(String id, String value) {
            object.addProperty(id, value);
         }

         @Override
         public void addProperty(String id, int value) {
            object.addProperty(id, value);
         }

         @Override
         public void addProperty(String id, boolean value) {
            object.addProperty(id, value);
         }

         @Override
         public void addNullProperty(String id) {
            object.add(id, JsonNull.INSTANCE);
         }
      };
   }
}
