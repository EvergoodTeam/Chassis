# Chassis <a href=https://www.curseforge.com/minecraft/mc-mods/chassis> <img src="http://cf.way2muchnoise.eu/596615.svg"> </a>

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
