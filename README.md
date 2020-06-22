# print-log介绍

**print-log**是通过[ASM](https://asm.ow2.io/index.html)在编译阶段直接操纵class字节码文件，为你项目中的Log日志添加行数。只在``debug``模式下有效。

# 引入

在根目录下中build.gradle添加:

```groovy
buildscript {
    repositories {
        jcenter()
        mavenCentral() // add repository
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:4.0.0'
        classpath 'com.phl:print-logline-plugin:0.0.2' // add plugin
    }
}
```

其次在``app/build.gradle``引用插件即可：

```groovy
apply plugin: 'com.android.application'
apply plugin: 'print-plugin' // apply plugin
```



# License

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