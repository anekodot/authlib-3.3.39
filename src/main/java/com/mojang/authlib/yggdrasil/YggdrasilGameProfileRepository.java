// Decompiled with VineFlower by @anekodot on Twitter & Discord. All rights go to the original project owners. Use of this project is subject to copyright laws, and you may be prosecuted and/or other actions taken upon you for using this code in any and all forms.

package com.mojang.authlib.yggdrasil;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mojang.authlib.Agent;
import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YggdrasilGameProfileRepository implements GameProfileRepository {
   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilGameProfileRepository.class);
   private final String searchPageUrl;
   private static final int ENTRIES_PER_PAGE = 2;
   private static final int MAX_FAIL_COUNT = 3;
   private static final int DELAY_BETWEEN_PAGES = 100;
   private static final int DELAY_BETWEEN_FAILURES = 750;
   private final YggdrasilAuthenticationService authenticationService;

   public YggdrasilGameProfileRepository(YggdrasilAuthenticationService authenticationService, Environment environment) {
      this.authenticationService = authenticationService;
      this.searchPageUrl = environment.getAccountsHost() + "/profiles/";
   }

   @Override
   public void findProfilesByNames(String[] names, Agent agent, ProfileLookupCallback callback) {
      Set<String> criteria = Sets.newHashSet();

      for (String name : names) {
         if (!Strings.isNullOrEmpty(name)) {
            criteria.add(name.toLowerCase());
         }
      }

      int page = 0;

      for (List<String> request : Iterables.partition(criteria, 2)) {
         int failCount = 0;

         boolean failed;
         do {
            failed = false;

            try {
               ProfileSearchResultsResponse response = this.authenticationService
                  .makeRequest(
                     HttpAuthenticationService.constantURL(this.searchPageUrl + agent.getName().toLowerCase()), request, ProfileSearchResultsResponse.class
                  );
               failCount = 0;
               LOGGER.debug("Page {} returned {} results, parsing", 0, response.getProfiles().length);
               Set<String> missing = Sets.newHashSet(request);

               for (GameProfile profile : response.getProfiles()) {
                  LOGGER.debug("Successfully looked up profile {}", profile);
                  missing.remove(profile.getName().toLowerCase());
                  callback.onProfileLookupSucceeded(profile);
               }

               for (String namex : missing) {
                  LOGGER.debug("Couldn't find profile {}", namex);
                  callback.onProfileLookupFailed(new GameProfile(null, namex), new ProfileNotFoundException("Server did not find the requested profile"));
               }

               try {
                  Thread.sleep(100L);
               } catch (InterruptedException var17) {
               }
            } catch (AuthenticationException var18) {
               AuthenticationException e = var18;
               if (++failCount == 3) {
                  for (String namex : request) {
                     LOGGER.debug("Couldn't find profile {} because of a server error", namex);
                     callback.onProfileLookupFailed(new GameProfile(null, namex), e);
                  }
               } else {
                  try {
                     Thread.sleep(750L);
                  } catch (InterruptedException var16) {
                  }

                  failed = true;
               }
            }
         } while (!failed);
      }
   }
}
