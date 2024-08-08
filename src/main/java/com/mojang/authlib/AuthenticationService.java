// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib;

import com.mojang.authlib.minecraft.MinecraftSessionService;

public interface AuthenticationService {
   UserAuthentication createUserAuthentication(Agent var1);

   MinecraftSessionService createMinecraftSessionService();

   GameProfileRepository createProfileRepository();
}
