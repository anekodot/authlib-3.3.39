// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;

public class ProfileSearchResultsResponse extends Response {
   private GameProfile[] profiles;

   public GameProfile[] getProfiles() {
      return this.profiles;
   }

   public static class Serializer implements JsonDeserializer<ProfileSearchResultsResponse> {
      public ProfileSearchResultsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         ProfileSearchResultsResponse result = new ProfileSearchResultsResponse();
         if (json instanceof JsonObject object) {
            if (object.has("error")) {
               result.setError(object.getAsJsonPrimitive("error").getAsString());
            }

            if (object.has("errorMessage")) {
               result.setError(object.getAsJsonPrimitive("errorMessage").getAsString());
            }

            if (object.has("cause")) {
               result.setError(object.getAsJsonPrimitive("cause").getAsString());
            }
         } else {
            result.profiles = (GameProfile[])context.deserialize(json, GameProfile[].class);
         }

         return result;
      }
   }
}
