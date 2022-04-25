# Chassis <a href=https://www.curseforge.com/minecraft/mc-mods/chassis> <img src="http://cf.way2muchnoise.eu/596615.svg"> </a>

Creating hundreds of Json files for some Blocks is REALLY time consuming, so you should automate that

Chassis is a Library providing utilities for dynamic asset generation (Blockstates, Models, Tags, Recipes, Loot Tables, Textures) and configuration for your Mod

## Adding to your own project

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
Make sure to change `<releaseVersion>`  
We recommend using `${project.chassis_version}` and specifying the version in your `gradle.properties`

### fabric.mod.json

- Versions < `1.1.0` = `1.18.1`
- Versions >= `1.1.0` = `1.18.2`

Each version from their respective group should not break when swapped for newer one

```
"depends": {
    ...
    "chassis": "1.0.x"
},
```
