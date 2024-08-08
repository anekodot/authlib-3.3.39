// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.minecraft;;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.net.InetAddress;
import java.util.Map;

public interface MinecraftSessionService {
   void joinServer(GameProfile var1, String var2, String var3) throws AuthenticationException;

   GameProfile hasJoinedServer(GameProfile var1, String var2, InetAddress var3) throws AuthenticationUnavailableException;

   Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile var1, boolean var2);

   GameProfile fillProfileProperties(GameProfile var1, boolean var2);
}
