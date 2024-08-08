// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.minecraft;;

import java.util.function.Consumer;

public interface TelemetrySession {
   TelemetrySession DISABLED = new TelemetrySession() {
      @Override
      public boolean isEnabled() {
         return false;
      }

      @Override
      public TelemetryPropertyContainer globalProperties() {
         return TelemetryEvent.EMPTY;
      }

      @Override
      public void eventSetupFunction(Consumer<TelemetryPropertyContainer> event) {
      }

      @Override
      public TelemetryEvent createNewEvent(String type) {
         return TelemetryEvent.EMPTY;
      }
   };

   boolean isEnabled();

   TelemetryPropertyContainer globalProperties();

   void eventSetupFunction(Consumer<TelemetryPropertyContainer> var1);

   TelemetryEvent createNewEvent(String var1);
}
