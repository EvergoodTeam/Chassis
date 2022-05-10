# Chassis <a href=https://www.curseforge.com/minecraft/mc-mods/chassis> <img src="http://cf.way2muchnoise.eu/596615.svg"> </a>

Creating hundreds of Json files for some Blocks is REALLY time consuming, so you should automate that

Chassis is a Library providing utilities for dynamic asset generation (Blockstates, Models, Tags, Recipes, Loot Tables,
Textures) and configuration for your Mod

## Adding to your own project

### How versions work

*Note that this system could be changed in the near future*

Our [GitHub Releases](https://github.com/EvergoodTeam/Chassis/releases) include many Builds of the project, each marked
with specific suffixes:

- `-dev`: in active development, unstable
- `-pre`: in the final stages of development and next to being finalised
- *no suffix*: final releases, that can also be found
  on [Curseforge](https://www.curseforge.com/minecraft/mc-mods/chassis)

Versions are grouped using Minecraft Versions

| Minecraft Version | `1.18.1` | `1.18.2` | `1.19` |
|:-----------------:|:--------:|:--------:|:------:|
|      Support      |    ✔️    |    ✔️    |   ❌    |
|  Chassis Version  | `1.0.x`  | `1.1.x`  |        |

Versions from the same Minecraft Version group have backwards compatibility

eg. `1.0.0` can be substituted with `1.0.5` and viceversa

### Gradle

In your `build.grade`

```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

```
dependencies {
    ...
    //Chassis
    modImplementation 'com.github.evergoodteam:chassis:<releaseVersion>'
}
```

Make sure to change the `<releaseVersion>` to an
appropriate [GitHub Release version](https://github.com/EvergoodTeam/Chassis/releases)  
We recommend using `${project.chassis_version}` and specifying the version in your `gradle.properties`

### fabric.mod.json

```
"depends": {
    ...
    "chassis": "1.x.x"
},
```

## Support us!

Want to support us? Make sure to use **CODE `Libra`** for **25% OFF** your order when renting a server over at [**BisectHosting**](https://www.bisecthosting.com/Libra)

## Discord

Join us on [**Discord**](https://discord.gg/k2P68Y8) for support and updates on upcoming versions

## Issues

If you encounter any issue during your playthrough, make sure to report it either here on [**GitHub**](https://github.com/EvergoodTeam/Chassis/issues) or on [**Discord**](https://discord.gg/k2P68Y8) in the
designated ```#issues``` channel

***

## License

[**Read Here**](https://github.com/EvergoodTeam/Chassis/blob/main/LICENSE)