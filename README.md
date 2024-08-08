# Authlib 3.3.39
Decompiled Authlib 3.3.39, made for my personal projects/MCP projects.

I'm not going to provide builds or support of any kind, and you use this project at your own risk.
The SHA1 of built-versions is different, so you'll need to remove the "downloads" key in your 1.18.2.json file. E.g.

This:
>         {
>            "downloads": {
>                "artifact": {
>                    "sha1": "2d7126c23720ca02ebf3dcf65f8fb5ccc999484b",
>                    "size": 98740,
>                    "url": "https://libraries.minecraft.net/com/mojang/authlib/3.3.39/authlib-3.3.39.jar"
>                }
>            },
>            "name": "com.mojang:authlib:3.3.39"
>        },
Turns into:
>   {
>       "name": "com.mojang:authlib:3.3.39"
>   },

This patch is known-working on PrismLauncher, however I'm not sure on other launchers/vanilla launcher.

To edit your instance json in Prism, follow these steps:
1. Go to your 1.18.2 instance, and click "edit"
2. Go to "Version", and click "Minecraft"
   3. On the sidebar, click "Customize", then "Edit"

### Minecraft versions this works with:
- 1.18.2

If you test this and find other versions this works with, make a PR and add here if you've got the time to. Thanks!

### Known Bugs:
- NPC skins aren't grabbed correctly, and fail to display in-game. Falls back to Steve/Alex skins.