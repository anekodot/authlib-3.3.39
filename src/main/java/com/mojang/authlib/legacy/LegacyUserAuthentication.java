// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.legacy;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.HttpUserAuthentication;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.util.UUIDTypeAdapter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

@Deprecated
public class LegacyUserAuthentication extends HttpUserAuthentication {
   private static final URL AUTHENTICATION_URL = HttpAuthenticationService.constantURL("https://login.minecraft.net");
   private static final int AUTHENTICATION_VERSION = 14;
   private static final int RESPONSE_PART_PROFILE_NAME = 2;
   private static final int RESPONSE_PART_SESSION_TOKEN = 3;
   private static final int RESPONSE_PART_PROFILE_ID = 4;
   private String sessionToken;

   protected LegacyUserAuthentication(LegacyAuthenticationService authenticationService) {
      super(authenticationService);
   }

   @Override
   public void logIn() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUsername())) {
         throw new InvalidCredentialsException("Invalid username");
      } else if (StringUtils.isBlank(this.getPassword())) {
         throw new InvalidCredentialsException("Invalid password");
      } else {
         Map<String, Object> args = new HashMap<>();
         args.put("user", this.getUsername());
         args.put("password", this.getPassword());
         args.put("version", 14);

         String response;
         try {
            response = this.getAuthenticationService()
               .performPostRequest(AUTHENTICATION_URL, HttpAuthenticationService.buildQuery(args), "application/x-www-form-urlencoded")
               .trim();
         } catch (IOException var7) {
            throw new AuthenticationException("Authentication server is not responding", var7);
         }

         String[] split = response.split(":");
         if (split.length == 5) {
            String profileId = split[4];
            String profileName = split[2];
            String sessionToken = split[3];
            if (!StringUtils.isBlank(profileId) && !StringUtils.isBlank(profileName) && !StringUtils.isBlank(sessionToken)) {
               this.setSelectedProfile(new GameProfile(UUIDTypeAdapter.fromString(profileId), profileName));
               this.sessionToken = sessionToken;
               this.setUserType(UserType.LEGACY);
            } else {
               throw new AuthenticationException("Unknown response from authentication server: " + response);
            }
         } else {
            throw new InvalidCredentialsException(response);
         }
      }
   }

   @Override
   public void logOut() {
      super.logOut();
      this.sessionToken = null;
   }

   @Override
   public boolean canPlayOnline() {
      return this.isLoggedIn() && this.getSelectedProfile() != null && this.getAuthenticatedToken() != null;
   }

   @Override
   public GameProfile[] getAvailableProfiles() {
      return this.getSelectedProfile() != null ? new GameProfile[]{this.getSelectedProfile()} : new GameProfile[0];
   }

   @Override
   public void selectGameProfile(GameProfile profile) throws AuthenticationException {
      throw new UnsupportedOperationException("Game profiles cannot be changed in the legacy authentication service");
   }

   @Override
   public String getAuthenticatedToken() {
      return this.sessionToken;
   }

   @Override
   public String getUserID() {
      return this.getUsername();
   }

   public LegacyAuthenticationService getAuthenticationService() {
      return (LegacyAuthenticationService)super.getAuthenticationService();
   }
}
