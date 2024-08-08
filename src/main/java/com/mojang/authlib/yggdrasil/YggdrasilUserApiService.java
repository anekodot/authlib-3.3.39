// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.mojang.authlib.Environment;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.response.BlockListResponse;
import com.mojang.authlib.yggdrasil.response.UserAttributesResponse;
import java.net.Proxy;
import java.net.URL;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class YggdrasilUserApiService implements UserApiService {
   private static final long BLOCKLIST_REQUEST_COOLDOWN_SECONDS = 120L;
   private static final UUID ZERO_UUID = new UUID(0L, 0L);
   private final URL routePrivileges;
   private final URL routeBlocklist;
   private final MinecraftClient minecraftClient;
   private final Environment environment;
   private UserApiService.UserProperties properties = OFFLINE_PROPERTIES;
   @Nullable
   private Instant nextAcceptableBlockRequest;
   @Nullable
   private Set<UUID> blockList;

   public YggdrasilUserApiService(String accessToken, Proxy proxy, Environment env) throws AuthenticationException {
      this.minecraftClient = new MinecraftClient(accessToken, proxy);
      this.environment = env;
      this.routePrivileges = HttpAuthenticationService.constantURL(env.getServicesHost() + "/player/attributes");
      this.routeBlocklist = HttpAuthenticationService.constantURL(env.getServicesHost() + "/privacy/blocklist");
      this.fetchProperties();
   }

   @Override
   public UserApiService.UserProperties properties() {
      return this.properties;
   }

   @Override
   public TelemetrySession newTelemetrySession(Executor executor) {
      return (TelemetrySession)(!this.properties.flag(UserApiService.UserFlag.TELEMETRY_ENABLED)
         ? TelemetrySession.DISABLED
         : new YggdrassilTelemetrySession(this.minecraftClient, this.environment, executor));
   }

   @Override
   public boolean isBlockedPlayer(UUID playerID) {
      if (playerID.equals(ZERO_UUID)) {
         return false;
      } else {
         if (this.blockList == null) {
            this.blockList = this.fetchBlockList();
            if (this.blockList == null) {
               return false;
            }
         }

         return this.blockList.contains(playerID);
      }
   }

   @Override
   public void refreshBlockList() {
      if (this.blockList == null || this.canMakeBlockListRequest()) {
         this.blockList = this.forceFetchBlockList();
      }
   }

   @Nullable
   private Set<UUID> fetchBlockList() {
      return !this.canMakeBlockListRequest() ? null : this.forceFetchBlockList();
   }

   private boolean canMakeBlockListRequest() {
      return this.nextAcceptableBlockRequest == null || Instant.now().isAfter(this.nextAcceptableBlockRequest);
   }

   private Set<UUID> forceFetchBlockList() {
      this.nextAcceptableBlockRequest = Instant.now().plusSeconds(120L);

      try {
         BlockListResponse response = this.minecraftClient.get(this.routeBlocklist, BlockListResponse.class);
         return response.getBlockedProfiles();
      } catch (MinecraftClientHttpException var2) {
         return null;
      } catch (MinecraftClientException var3) {
         return null;
      }
   }

   private void fetchProperties() throws AuthenticationException {
      try {
         UserAttributesResponse response = this.minecraftClient.get(this.routePrivileges, UserAttributesResponse.class);
         Builder<UserApiService.UserFlag> flags = ImmutableSet.builder();
         UserAttributesResponse.Privileges privileges = response.getPrivileges();
         if (privileges != null) {
            addFlagIfUserHasPrivilege(privileges.getOnlineChat(), UserApiService.UserFlag.CHAT_ALLOWED, flags);
            addFlagIfUserHasPrivilege(privileges.getMultiplayerServer(), UserApiService.UserFlag.SERVERS_ALLOWED, flags);
            addFlagIfUserHasPrivilege(privileges.getMultiplayerRealms(), UserApiService.UserFlag.REALMS_ALLOWED, flags);
            addFlagIfUserHasPrivilege(privileges.getTelemetry(), UserApiService.UserFlag.TELEMETRY_ENABLED, flags);
         }

         UserAttributesResponse.ProfanityFilterPreferences profanityFilterPreferences = response.getProfanityFilterPreferences();
         if (profanityFilterPreferences != null && profanityFilterPreferences.isEnabled()) {
            flags.add(UserApiService.UserFlag.PROFANITY_FILTER_ENABLED);
         }

         this.properties = new UserApiService.UserProperties(flags.build());
      } catch (MinecraftClientHttpException var5) {
         throw var5.toAuthenticationException();
      } catch (MinecraftClientException var6) {
         throw var6.toAuthenticationException();
      }
   }

   private static void addFlagIfUserHasPrivilege(boolean privilege, UserApiService.UserFlag value, Builder<UserApiService.UserFlag> output) {
      if (privilege) {
         output.add(value);
      }
   }
}
