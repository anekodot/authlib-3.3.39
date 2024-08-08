// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.Environment;
import com.mojang.authlib.EnvironmentParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.UserMigratedException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.util.UUIDTypeAdapter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YggdrasilAuthenticationService extends HttpAuthenticationService {
   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilAuthenticationService.class);
   @Nullable
   private final String clientToken;
   private final Gson gson;
   private final Environment environment;

   public YggdrasilAuthenticationService(Proxy proxy) {
      this(proxy, determineEnvironment());
   }

   public YggdrasilAuthenticationService(Proxy proxy, Environment environment) {
      this(proxy, null, environment);
   }

   public YggdrasilAuthenticationService(Proxy proxy, @Nullable String clientToken) {
      this(proxy, clientToken, determineEnvironment());
   }

   public YggdrasilAuthenticationService(Proxy proxy, @Nullable String clientToken, Environment environment) {
      super(proxy);
      this.clientToken = clientToken;
      this.environment = environment;
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(GameProfile.class, new GameProfileSerializer());
      builder.registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer());
      builder.registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
      builder.registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer());
      this.gson = builder.create();
      LOGGER.info("Environment: " + environment.asString());
   }

   private static Environment determineEnvironment() {
      return EnvironmentParser.getEnvironmentFromProperties().orElse(YggdrasilEnvironment.PROD.getEnvironment());
   }

   @Override
   public UserAuthentication createUserAuthentication(Agent agent) {
      if (this.clientToken == null) {
         throw new IllegalStateException("Missing client token");
      } else {
         return new YggdrasilUserAuthentication(this, this.clientToken, agent, this.environment);
      }
   }

   @Override
   public MinecraftSessionService createMinecraftSessionService() {
      return new YggdrasilMinecraftSessionService(this, this.environment);
   }

   @Override
   public GameProfileRepository createProfileRepository() {
      return new YggdrasilGameProfileRepository(this, this.environment);
   }

   protected <T extends Response> T makeRequest(URL url, Object input, Class<T> classOfT) throws AuthenticationException {
      return this.makeRequest(url, input, classOfT, null);
   }

   protected <T extends Response> T makeRequest(URL url, Object input, Class<T> classOfT, @Nullable String authentication) throws AuthenticationException {
      try {
         String jsonResult = input == null
            ? this.performGetRequest(url, authentication)
            : this.performPostRequest(url, this.gson.toJson(input), "application/json");
         T result = (T)this.gson.fromJson(jsonResult, classOfT);
         if (result == null) {
            return null;
         } else if (StringUtils.isNotBlank(result.getError())) {
            if ("UserMigratedException".equals(result.getCause())) {
               throw new UserMigratedException(result.getErrorMessage());
            } else if ("ForbiddenOperationException".equals(result.getError())) {
               throw new InvalidCredentialsException(result.getErrorMessage());
            } else if ("InsufficientPrivilegesException".equals(result.getError())) {
               throw new InsufficientPrivilegesException(result.getErrorMessage());
            } else {
               throw new AuthenticationException(result.getErrorMessage());
            }
         } else {
            return result;
         }
      } catch (IllegalStateException | JsonParseException | IOException var7) {
         throw new AuthenticationUnavailableException("Cannot contact authentication server", var7);
      }
   }

   public UserApiService createUserApiService(String accessToken) throws AuthenticationException {
      return new YggdrasilUserApiService(accessToken, this.getProxy(), this.environment);
   }

   private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {
      public GameProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         JsonObject object = (JsonObject)json;
         UUID id = object.has("id") ? (UUID)context.deserialize(object.get("id"), UUID.class) : null;
         String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
         return new GameProfile(id, name);
      }

      public JsonElement serialize(GameProfile src, Type typeOfSrc, JsonSerializationContext context) {
         JsonObject result = new JsonObject();
         if (src.getId() != null) {
            result.add("id", context.serialize(src.getId()));
         }

         if (src.getName() != null) {
            result.addProperty("name", src.getName());
         }

         return result;
      }
   }
}
