// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.properties;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map.Entry;

public class PropertyMap extends ForwardingMultimap<String, Property> {
   private final Multimap<String, Property> properties = LinkedHashMultimap.create();

   protected Multimap<String, Property> delegate() {
      return this.properties;
   }

   public static class Serializer implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {
      public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         PropertyMap result = new PropertyMap();
         if (json instanceof JsonObject object) {
            for (Entry<String, JsonElement> entry : object.entrySet()) {
               if (entry.getValue() instanceof JsonArray) {
                  for (JsonElement element : (JsonArray)entry.getValue()) {
                     result.put(entry.getKey(), new Property(entry.getKey(), element.getAsString()));
                  }
               }
            }
         } else if (json instanceof JsonArray) {
            for (JsonElement element : (JsonArray)json) {
               if (element instanceof JsonObject) {
                  JsonObject object = (JsonObject)element;
                  String name = object.getAsJsonPrimitive("name").getAsString();
                  String value = object.getAsJsonPrimitive("value").getAsString();
                  if (object.has("signature")) {
                     result.put(name, new Property(name, value, object.getAsJsonPrimitive("signature").getAsString()));
                  } else {
                     result.put(name, new Property(name, value));
                  }
               }
            }
         }

         return result;
      }

      public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
         JsonArray result = new JsonArray();

         for (Property property : src.values()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", property.getName());
            object.addProperty("value", property.getValue());
            if (property.hasSignature()) {
               object.addProperty("signature", property.getSignature());
            }

            result.add(object);
         }

         return result;
      }
   }
}
