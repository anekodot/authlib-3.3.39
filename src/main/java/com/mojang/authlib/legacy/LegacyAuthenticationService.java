// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.legacy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import java.net.Proxy;
import org.apache.commons.lang3.Validate;

@Deprecated
public class LegacyAuthenticationService extends HttpAuthenticationService {
   protected LegacyAuthenticationService(Proxy proxy) {
      super(proxy);
   }

   public LegacyUserAuthentication createUserAuthentication(Agent agent) {
      Validate.notNull(agent);
      if (agent != Agent.MINECRAFT) {
         throw new IllegalArgumentException("Legacy authentication cannot handle anything but Minecraft");
      } else {
         return new LegacyUserAuthentication(this);
      }
   }

   public LegacyMinecraftSessionService createMinecraftSessionService() {
      return new LegacyMinecraftSessionService(this);
   }

   @Override
   public GameProfileRepository createProfileRepository() {
      throw new UnsupportedOperationException("Legacy authentication service has no profile repository");
   }
}
