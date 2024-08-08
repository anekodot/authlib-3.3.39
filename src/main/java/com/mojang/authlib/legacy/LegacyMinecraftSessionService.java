// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.legacy;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.HttpMinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class LegacyMinecraftSessionService extends HttpMinecraftSessionService {
   private static final String BASE_URL = "http://session.minecraft.net/game/";
   private static final URL JOIN_URL = HttpAuthenticationService.constantURL("http://session.minecraft.net/game/joinserver.jsp");
   private static final URL CHECK_URL = HttpAuthenticationService.constantURL("http://session.minecraft.net/game/checkserver.jsp");

   protected LegacyMinecraftSessionService(LegacyAuthenticationService authenticationService) {
      super(authenticationService);
   }

   @Override
   public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws AuthenticationException {
      Map<String, Object> arguments = new HashMap<>();
      arguments.put("user", profile.getName());
      arguments.put("sessionId", authenticationToken);
      arguments.put("serverId", serverId);
      URL url = HttpAuthenticationService.concatenateURL(JOIN_URL, HttpAuthenticationService.buildQuery(arguments));

      try {
         String response = this.getAuthenticationService().performGetRequest(url);
         if (!"OK".equals(response)) {
            throw new AuthenticationException(response);
         }
      } catch (IOException var7) {
         throw new AuthenticationUnavailableException(var7);
      }
   }

   @Override
   public GameProfile hasJoinedServer(GameProfile user, String serverId, InetAddress address) throws AuthenticationUnavailableException {
      Map<String, Object> arguments = new HashMap<>();
      arguments.put("user", user.getName());
      arguments.put("serverId", serverId);
      URL url = HttpAuthenticationService.concatenateURL(CHECK_URL, HttpAuthenticationService.buildQuery(arguments));

      try {
         String response = this.getAuthenticationService().performGetRequest(url);
         return "YES".equals(response) ? user : null;
      } catch (IOException var7) {
         throw new AuthenticationUnavailableException(var7);
      }
   }

   @Override
   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile profile, boolean requireSecure) {
      return new HashMap<>();
   }

   @Override
   public GameProfile fillProfileProperties(GameProfile profile, boolean requireSecure) {
      return profile;
   }

   public LegacyAuthenticationService getAuthenticationService() {
      return (LegacyAuthenticationService)super.getAuthenticationService();
   }
}
