// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Environment;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum YggdrasilEnvironment {
   PROD("https://authserver.mojang.com", "https://api.mojang.com", "https://sessionserver.mojang.com", "https://api.minecraftservices.com"),
   STAGING(
      "https://yggdrasil-auth-staging.mojang.com",
      "https://api-staging.mojang.com",
      "https://api-sb-staging.minecraftservices.com",
      "https://api-staging.minecraftservices.com"
   );

   private final Environment environment;

   private YggdrasilEnvironment(String authHost, String accountsHost, String sessionHost, String servicesHost) {
      this.environment = Environment.create(authHost, accountsHost, sessionHost, servicesHost, this.name());
   }

   public Environment getEnvironment() {
      return this.environment;
   }

   public static Optional<Environment> fromString(@Nullable String value) {
      return Stream.of(values()).filter(env -> value != null && value.equalsIgnoreCase(env.name())).findFirst().map(YggdrasilEnvironment::getEnvironment);
   }
}
