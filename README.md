# Chassis <a href=https://www.curseforge.com/minecraft/mc-mods/chassis> <img src="http://cf.way2muchnoise.eu/596615.svg"> </a>

Creating hundreds of Json files for some Blocks is REALLY time-consuming, so you should automate that

Chassis is a Library providing utilities for dynamic asset generation (Blockstates, Models, Tags, Recipes, Loot Tables,
Textures) and simple configuration.  
A showcase of the available features can be found (commented) in the source code or
in [Compressor](https://www.curseforge.com/minecraft/mc-mods/compressor).

## Adding to your own Project

### How versions work

*NOTE: this system could be changed in the near future*

[GitHub Releases](https://github.com/EvergoodTeam/Chassis/releases) include different Builds of the project, spanning
from those in active development to final ones.

Each version name is composed of two main parts: a <ins>version number</ins> (`1.2.34`) and a <ins>
suffix</ins> (`-suffix`).

Different <ins>suffixes</ins> are used to highlight the nature and state of a Build:

- `-dev`: in active development, likely unstable and temporary
- `-pre`: in the final stages of development, next to being finalised and released officially
- *no suffix*: final releases, can be found on [Curseforge](https://www.curseforge.com/minecraft/mc-mods/chassis)

<ins>Version numbers</ins> are grouped together based on Minecraft's Versions:  
this means that Builds from the same group are compatible only with that specific Minecraft Version and that they're
interchangeable with one another.

The table below summarizes the supported Minecraft Versions

| Minecraft Version | `1.12.2` | `1.18.1` | `1.18.2` | `1.19`  |
|:-----------------:|:--------:|:--------:|:--------:|:-------:|
|      Support      |    ❌     |    ✔️    |    ✔️    |   ✔️    |
|  Chassis Version  |    ?     | `1.0.x`  | `1.1.x`  | `1.2.x` |

### Gradle

In your `build.grade`, add the Project as a dependency

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

Don't forget to change `<releaseVersion>` to an appropriate version, as explained **[before](#how-versions-work)**  
We recommend using `${project.chassis_version}` and specifying the version in your `gradle.properties` for easier access

### fabric.mod.json

In your `fabric.mod.json`, add the Project as a dependency

```
"depends": {
    ...
    "chassis": "1.x.x"
},
```

***

## Support us!

Want to support us? Make sure to use **CODE `Libra`** for **25% OFF** your order when renting a server over at **[
BisectHosting](https://www.bisecthosting.com/Libra)**

## Discord

Join us on **[Discord](https://discord.gg/k2P68Y8)** for support and updates on upcoming versions

## Issues

If you encounter any issue during your playthrough, make sure to report it either here on **[
GitHub](https://github.com/EvergoodTeam/Chassis/issues)** or on **[Discord](https://discord.gg/k2P68Y8)** in the
designated ```#issues``` channel

***

## License

[Read Here](https://github.com/EvergoodTeam/Chassis/blob/main/LICENSE)