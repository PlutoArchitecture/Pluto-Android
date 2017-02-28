# Pluto
[![License MIT](https://img.shields.io/badge/license-MIT-green.svg?style=flat)](https://raw.githubusercontent.com/minggo620/Pluto-Android/master/LICENSE)&nbsp;
[![Travis](https://img.shields.io/travis/rust-lang/rust.svg)]()
[![Github All Releases](https://img.shields.io/badge/download-1.4M Total-green.svg)](https://codeload.github.com/minggo620/Pluto-Android/zip/master)  

High integrated development framework for Android applications.<br/>
Features
==============
- **Cache Data High Integrated**:The Data operation(Sqlite, SharePreference, File) is integrated as a DataFramework, and provide a DataManagerProxy to operating cache data .
- **Network Data and Cache Data High Integrated**: The Data from network add the data from DataFramework are integrated in PlutoApiEngine.
- **Service High Integrated**: The special business logic and PlutoApiEngine are integrated as a LogicManager, and put the logic in a CommonAsyncTask process.
- **UI Framework High Integrated**: There is no need to add many component of Android, just extends PlutoActivity, It contains Toast, PlutoDialog, SoftInputManager, Handler and so on.More, through using LogicManager chain programming, it can easier and faster finish data ,logic and UI refresh work. 
- **Flexible Utils**: There are many kinds of common tools being provided for developer to deal many kind of trivial work，such as clipping Bitmap, formate Date, defined Log and so on.
- **Global Exception Handle** PlutoException can handle app global Exception if they are happening.

Architecture diagram
================
![Pluto架构图](http://upload-images.jianshu.io/upload_images/1252638-594c275b66a53e9b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
