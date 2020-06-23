# print-log

[中文](README_zh.md)

Pint-log is a android gradle plugin based on [ASM](https://asm.ow2.io/) and [Gradle Transform API](http://tools.android.com/tech-docs/new-build-system/transform-api).  you can add a line number into every lines of your logcat. Only debug mode available.

## Gradle compatibility

The plugin officially supports only Gradle 6.1.1+.

## Add print-log to your project

Add the following Gradle configuration to your Android project. In your root ``build.gradle`` file:

```groovy
buildscript {
    repositories {
        jcenter()
        mavenCentral() // add repository
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:4.0.0'
        classpath 'com.phl:print-logline-plugin:0.0.3' // add plugin
    }
}
```

In your app modules ``app/build.gradle`` file:

```groovy
apply plugin: 'com.android.application'
apply plugin: 'print-plugin' // apply plugin
```

If your project contains other Log dependencies，you can exclude it. In your app modules ``app/build.gradle`` file  add ``printExt`` extension:

```groovy
apply plugin: 'com.android.application'

//add ignore packages
printExt{
    ignoreClass=["com.phl.androidlogprint.util","com.orhanobut.logger"]
}

dependencies {
    implementation 'com.orhanobut:logger:2.2.0'
}
```



## Thanks

- [Hunter](https://github.com/Leaking/Hunter)

- [ASM](https://asm.ow2.io/asm4-guide.pdf) asm4-guide.pdf

## License

```
   Copyright CoderHL [https://github.com/penghuanliang/print-log]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

