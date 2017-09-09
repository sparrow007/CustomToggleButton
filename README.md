# CustomToggleButton in Android
This Project let you create the custom toggleButton with beautiful animation in a simplest way

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-13%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=13)

![togglelib](https://user-images.githubusercontent.com/22986571/30244134-a86b220c-95d5-11e7-90c8-7f42f1e2744f.gif)

USAGE
-----
To make a Custom ToggleButton add CustomToggle in your layout XML and add CustomToggleButton library in your project or you can also grab it through Gradle:

Gradle
------
```
dependencies {
    ...
   compile 'com.jackandphantom.android:customtogglebutton:1.0.0'
}
```
XML
-----

```xml
<!-- <a> custom togglebutton xml</a> -->
<com.jackandphantom.customtogglebutton.CustomToggle
    android:layout_width="275dp"
    android:layout_height="100dp"
    app:ctg_addLeftIcon="@drawable/ic_left_icon"
    app:ctg_addRightIcon="@drawable/ic_right_icon"
    app:ctg_addSlideColor="#ff67"
    app:ctg_addSlideBackgroundColor="#6c817a"
    app:ctg_addAnimationType="jack_animation"
    />
```
NOTE
-----
You should use vector drawable icon in ctg_addLeftIcon and ctg_addRightIcon attribute.

PROPERTIES
-----
You may use the following properties in your XML to change your CustomToggleButton.

/*  circular progressbar xml */
*  app:ctg_addLeftIcon              (Drawable)  -->   default NULL
*  app:ctg_addRightIcon             (Drawable)  -->   default NULL
*  app:ctg_addMagnification         (Integer)   -->   default 8
*  app:ctg_addSlideColor            (Color)     -->   default BLUE
*  app:ctg_addSlideBackgroundColor  (Color)     -->   default LTGRAY
*  app:ctg_addAnimationType         (Enum)      -->   default jack_animation
*  app:ctg_addAnimationTime         (Integer)   -->   default 600

JAVA
-----
```java
  CustomToggle customToggle = (CustomToggle) findViewById(R.id.custom);
      customToggle.addFirstIcon(firstIconDrawable);
      customToggle.addSecondIcon(secondIconDrawable);
      customToggle.setMagnification(9);
      customToggle.setSlideBackgroundColor(Color.BLACK);
      customToggle.setAnimationTime(700);
      customToggle.setSlideColor(Color.GREEN);
      customToggle.setOnToggleClickListener(new CustomToggle.OnToggleClickListener() {
                        @Override
                        public void onLefToggleEnabled(boolean enabled) {
                            
                        }

                        @Override
                        public void onRightToggleEnabled(boolean enabled) {

                        }
                    });
```
* There is also the getter method you can call them as per your requirements.

LICENCE
-----

 Copyright 2017 Ankit kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 Thanks to  Vitaly Rubtsov 
