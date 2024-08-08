// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.minecraft;;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

public interface UserApiService {
   UserApiService.UserProperties OFFLINE_PROPERTIES = new UserApiService.UserProperties(
      Set.of(UserFlag.CHAT_ALLOWED, UserFlag.REALMS_ALLOWED, UserFlag.SERVERS_ALLOWED)
   );
   UserApiService OFFLINE = new UserApiService() {
      @Override
      public UserApiService.UserProperties properties() {
         return OFFLINE_PROPERTIES;
      }

      @Override
      public boolean isBlockedPlayer(UUID playerID) {
         return false;
      }

      @Override
      public void refreshBlockList() {
      }

      @Override
      public TelemetrySession newTelemetrySession(Executor executor) {
         return TelemetrySession.DISABLED;
      }
   };

   UserApiService.UserProperties properties();

   boolean isBlockedPlayer(UUID var1);

   void refreshBlockList();

   TelemetrySession newTelemetrySession(Executor var1);

   public static enum UserFlag {
      SERVERS_ALLOWED,
      REALMS_ALLOWED,
      CHAT_ALLOWED,
      TELEMETRY_ENABLED,
      PROFANITY_FILTER_ENABLED;
   }

   public static record UserProperties(Set<UserFlag> flags) {
      public boolean flag(UserApiService.UserFlag flag) {
         return this.flags.contains(flag);
      }
   }
}
