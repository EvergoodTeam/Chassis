# Chassis <a href=https://www.curseforge.com/minecraft/mc-mods/chassis> <img src="http://cf.way2muchnoise.eu/596615.svg"> </a>

Creating hundreds of Json files for some Blocks is REALLY time consuming, so you should automate that

Chassis is a Library providing utilities for dynamic asset generation (Blockstates, Models, Tags, Recipes, Loot Tables, Textures) and configuration for your Mod

## Adding to your own project

### Gradle

```
repositories {

	maven {
		url "https://jitpack.io"
		content { includeGroup "com.github.evergoodteam" }
	}
}
```

```
dependencies {

	//Chassis
	modImplementation 'com.github.evergoodteam:chassis:<releaseVersion>'
}
```

### fabric.mod.json

```
[...]
"depends": {
    [...]
    "chassis": "0.x.x"
  },
```
