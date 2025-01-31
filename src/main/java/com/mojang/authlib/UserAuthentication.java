// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.properties.PropertyMap;
import java.util.Map;

public interface UserAuthentication {
   boolean canLogIn();

   void logIn() throws AuthenticationException;

   void logOut();

   boolean isLoggedIn();

   boolean canPlayOnline();

   GameProfile[] getAvailableProfiles();

   GameProfile getSelectedProfile();

   void selectGameProfile(GameProfile var1) throws AuthenticationException;

   void loadFromStorage(Map<String, Object> var1);

   Map<String, Object> saveForStorage();

   void setUsername(String var1);

   void setPassword(String var1);

   String getAuthenticatedToken();

   String getUserID();

   PropertyMap getUserProperties();

   UserType getUserType();
}
