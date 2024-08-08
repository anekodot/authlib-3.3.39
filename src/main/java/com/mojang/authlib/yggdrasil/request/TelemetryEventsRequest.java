// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil.request;

import com.google.gson.JsonObject;
import java.time.Instant;
import java.util.List;

public class TelemetryEventsRequest {
   public final List<Event> events;

   public TelemetryEventsRequest(List<Event> events) {
      this.events = events;
   }

   public static class Event {
      public final String source;
      public final String name;
      public final long timestamp;
      public final JsonObject data;

      public Event(String source, String name, Instant timestamp, JsonObject data) {
         this.source = source;
         this.name = name;
         this.timestamp = timestamp.getEpochSecond();
         this.data = data;
      }
   }
}
