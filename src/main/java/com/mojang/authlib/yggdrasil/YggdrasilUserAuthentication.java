// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Agent;
import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.HttpUserAuthentication;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.yggdrasil.request.AuthenticationRequest;
import com.mojang.authlib.yggdrasil.request.RefreshRequest;
import com.mojang.authlib.yggdrasil.request.ValidateRequest;
import com.mojang.authlib.yggdrasil.response.AuthenticationResponse;
import com.mojang.authlib.yggdrasil.response.RefreshResponse;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.authlib.yggdrasil.response.User;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YggdrasilUserAuthentication extends HttpUserAuthentication {
   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilUserAuthentication.class);
   private final URL routeAuthenticate;
   private final URL routeRefresh;
   private final URL routeValidate;
   private final URL routeInvalidate;
   private final URL routeSignout;
   private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
   private final Agent agent;
   private GameProfile[] profiles;
   private final String clientToken;
   private String accessToken;
   private boolean isOnline;

   public YggdrasilUserAuthentication(YggdrasilAuthenticationService authenticationService, String clientToken, Agent agent) {
      this(authenticationService, clientToken, agent, YggdrasilEnvironment.PROD.getEnvironment());
   }

   public YggdrasilUserAuthentication(YggdrasilAuthenticationService authenticationService, String clientToken, Agent agent, Environment env) {
      super(authenticationService);
      this.clientToken = clientToken;
      this.agent = agent;
      LOGGER.info("Environment: " + env.getName(), ". AuthHost: " + env.getAuthHost());
      this.routeAuthenticate = HttpAuthenticationService.constantURL(env.getAuthHost() + "/authenticate");
      this.routeRefresh = HttpAuthenticationService.constantURL(env.getAuthHost() + "/refresh");
      this.routeValidate = HttpAuthenticationService.constantURL(env.getAuthHost() + "/validate");
      this.routeInvalidate = HttpAuthenticationService.constantURL(env.getAuthHost() + "/invalidate");
      this.routeSignout = HttpAuthenticationService.constantURL(env.getAuthHost() + "/signout");
   }

   @Override
   public boolean canLogIn() {
      return !this.canPlayOnline()
         && StringUtils.isNotBlank(this.getUsername())
         && (StringUtils.isNotBlank(this.getPassword()) || StringUtils.isNotBlank(this.getAuthenticatedToken()));
   }

   @Override
   public void logIn() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUsername())) {
         throw new InvalidCredentialsException("Invalid username");
      } else {
         if (StringUtils.isNotBlank(this.getAuthenticatedToken())) {
            this.logInWithToken();
         } else {
            if (!StringUtils.isNotBlank(this.getPassword())) {
               throw new InvalidCredentialsException("Invalid password");
            }

            this.logInWithPassword();
         }
      }
   }

   protected void logInWithPassword() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUsername())) {
         throw new InvalidCredentialsException("Invalid username");
      } else if (StringUtils.isBlank(this.getPassword())) {
         throw new InvalidCredentialsException("Invalid password");
      } else {
         LOGGER.info("Logging in with username & password");
         AuthenticationRequest request = new AuthenticationRequest(this.getAgent(), this.getUsername(), this.getPassword(), this.clientToken);
         AuthenticationResponse response = this.getAuthenticationService().makeRequest(this.routeAuthenticate, request, AuthenticationResponse.class);
         if (!response.getClientToken().equals(this.clientToken)) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
         } else {
            if (response.getSelectedProfile() != null) {
               this.setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
            } else if (ArrayUtils.isNotEmpty(response.getAvailableProfiles())) {
               this.setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
            }

            User user = response.getUser();
            if (user != null && user.getId() != null) {
               this.setUserid(user.getId());
            } else {
               this.setUserid(this.getUsername());
            }

            this.isOnline = true;
            this.accessToken = response.getAccessToken();
            this.profiles = response.getAvailableProfiles();
            this.setSelectedProfile(response.getSelectedProfile());
            this.getModifiableUserProperties().clear();
            this.updateUserProperties(user);
         }
      }
   }

   protected void updateUserProperties(User user) {
      if (user != null) {
         if (user.getProperties() != null) {
            this.getModifiableUserProperties().putAll(user.getProperties());
         }
      }
   }

   protected void logInWithToken() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUserID())) {
         if (!StringUtils.isBlank(this.getUsername())) {
            throw new InvalidCredentialsException("Invalid uuid & username");
         }

         this.setUserid(this.getUsername());
      }

      if (StringUtils.isBlank(this.getAuthenticatedToken())) {
         throw new InvalidCredentialsException("Invalid access token");
      } else {
         LOGGER.info("Logging in with access token");
         if (this.checkTokenValidity()) {
            LOGGER.debug("Skipping refresh call as we're safely logged in.");
            this.isOnline = true;
         } else {
            RefreshRequest request = new RefreshRequest(this.getAuthenticatedToken(), this.clientToken);
            RefreshResponse response = this.getAuthenticationService().makeRequest(this.routeRefresh, request, RefreshResponse.class);
            if (!response.getClientToken().equals(this.clientToken)) {
               throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
            } else {
               if (response.getSelectedProfile() != null) {
                  this.setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
               } else if (ArrayUtils.isNotEmpty(response.getAvailableProfiles())) {
                  this.setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
               }

               if (response.getUser() != null && response.getUser().getId() != null) {
                  this.setUserid(response.getUser().getId());
               } else {
                  this.setUserid(this.getUsername());
               }

               this.isOnline = true;
               this.accessToken = response.getAccessToken();
               this.profiles = response.getAvailableProfiles();
               this.setSelectedProfile(response.getSelectedProfile());
               this.getModifiableUserProperties().clear();
               this.updateUserProperties(response.getUser());
            }
         }
      }
   }

   protected boolean checkTokenValidity() throws AuthenticationException {
      ValidateRequest request = new ValidateRequest(this.getAuthenticatedToken(), this.clientToken);

      try {
         this.getAuthenticationService().makeRequest(this.routeValidate, request, Response.class);
         return true;
      } catch (AuthenticationException var3) {
         return false;
      }
   }

   @Override
   public void logOut() {
      super.logOut();
      this.accessToken = null;
      this.profiles = null;
      this.isOnline = false;
   }

   @Override
   public GameProfile[] getAvailableProfiles() {
      return this.profiles;
   }

   @Override
   public boolean isLoggedIn() {
      return StringUtils.isNotBlank(this.accessToken);
   }

   @Override
   public boolean canPlayOnline() {
      return this.isLoggedIn() && this.getSelectedProfile() != null && this.isOnline;
   }

   @Override
   public void selectGameProfile(GameProfile profile) throws AuthenticationException {
      if (!this.isLoggedIn()) {
         throw new AuthenticationException("Cannot change game profile whilst not logged in");
      } else if (this.getSelectedProfile() != null) {
         throw new AuthenticationException("Cannot change game profile. You must log out and back in.");
      } else if (profile != null && ArrayUtils.contains(this.profiles, profile)) {
         RefreshRequest request = new RefreshRequest(this.getAuthenticatedToken(), this.clientToken, profile);
         RefreshResponse response = this.getAuthenticationService().makeRequest(this.routeRefresh, request, RefreshResponse.class);
         if (!response.getClientToken().equals(this.clientToken)) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
         } else {
            this.isOnline = true;
            this.accessToken = response.getAccessToken();
            this.setSelectedProfile(response.getSelectedProfile());
         }
      } else {
         throw new IllegalArgumentException("Invalid profile '" + profile + "'");
      }
   }

   @Override
   public void loadFromStorage(Map<String, Object> credentials) {
      super.loadFromStorage(credentials);
      this.accessToken = String.valueOf(credentials.get("accessToken"));
   }

   @Override
   public Map<String, Object> saveForStorage() {
      Map<String, Object> result = super.saveForStorage();
      if (StringUtils.isNotBlank(this.getAuthenticatedToken())) {
         result.put("accessToken", this.getAuthenticatedToken());
      }

      return result;
   }

   @Deprecated
   public String getSessionToken() {
      return this.isLoggedIn() && this.getSelectedProfile() != null && this.canPlayOnline()
         ? String.format("token:%s:%s", this.getAuthenticatedToken(), this.getSelectedProfile().getId())
         : null;
   }

   @Override
   public String getAuthenticatedToken() {
      return this.accessToken;
   }

   public Agent getAgent() {
      return this.agent;
   }

   @Override
   public String toString() {
      return "YggdrasilAuthenticationService{agent="
         + this.agent
         + ", profiles="
         + Arrays.toString((Object[])this.profiles)
         + ", selectedProfile="
         + this.getSelectedProfile()
         + ", username='"
         + this.getUsername()
         + "', isLoggedIn="
         + this.isLoggedIn()
         + ", userType="
         + this.getUserType()
         + ", canPlayOnline="
         + this.canPlayOnline()
         + ", accessToken='"
         + this.accessToken
         + "', clientToken='"
         + this.clientToken
         + "'}";
   }

   public YggdrasilAuthenticationService getAuthenticationService() {
      return (YggdrasilAuthenticationService)super.getAuthenticationService();
   }
}
