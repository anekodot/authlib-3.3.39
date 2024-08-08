// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.minecraft.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Objects;
import java.util.UUID;

public class ObjectMapper {
   private final Gson gson;

   public ObjectMapper(Gson gson) {
      this.gson = Objects.requireNonNull(gson);
   }

   public <T> T readValue(String value, Class<T> type) {
      try {
         return (T)this.gson.fromJson(value, type);
      } catch (JsonParseException var4) {
         throw new MinecraftClientException(MinecraftClientException.ErrorType.JSON_ERROR, "Failed to read value " + value, var4);
      }
   }

   public String writeValueAsString(Object entity) {
      try {
         return this.gson.toJson(entity);
      } catch (RuntimeException var3) {
         throw new MinecraftClientException(MinecraftClientException.ErrorType.JSON_ERROR, "Failed to write value", var3);
      }
   }

   public static ObjectMapper create() {
      return new ObjectMapper(new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create());
   }
}
