#### 目录介绍
- 01.学习JNI开发流程
    - 1.1 JNI开发概念
    - 1.2 JNI和NDK的关系
    - 1.3 JNI实践步骤
    - 1.4 NDK使用场景
- 02.NDK架构分层
    - 2.1 NDK分层构建层
    - 2.2 NDK分层Java层
    - 2.3 Native层
- 03.JNI基础语法
    - 3.1 JNI三种引用
    - 3.2 JNI异常处理
    - 3.3 C和C++互相调用
    - 3.4 JNI核心原理
    - 3.5 注册Native函数
- 04.一些必备操作
    - 4.1 so库生成打包
- 05.实践几个案例
    - 5.1 Java调用C/C++
    - 5.2 C/C++调用Java
    - 5.3 Java调用so中API




### 01.学习JNI开发流程
#### 1.1 JNI开发概念
- .SO库是什么东西
    - NDK为了方便使用，提供了一些脚本，使得更容易的编译C/C++代码。在Android程序编译中会将C/C++编译成动态库.so，类似java库.jar文件一样，它的生成需要使用NDK工具来打包。
    - so是shared object的缩写，见名思义就是共享的对象，机器可以直接运行的二进制代码。实质so文件就是一堆C、C++的头文件和实现文件打包成一个库。
- JNI是什么东西
    - JNI的全称是Java Native Interface，即本地Java接口。因为 Java 具备跨平台的特点，所以Java 与 本地代码交互的能力非常弱。
    - 采用JNI特性可以增强 Java 与本地代码交互的能力，使Java和其他类型的语言如C++/C能够互相调用。


#### 1.2 JNI和NDK的关系
- JNI和NDK学习内容太难
    - 其实难的不是JNI和NDK，而是C/C++语言，JNI和NDK只是个工具，很容易学习的。
- JNI和NDK有何联系
    - 学习JNI之前，首先得先知道JNI、NDK、Java和C/C++之间的关系。
    - 在Android开发中，有时为了性能和安全性（反编译），需要使用C/C++语言，但是Android APP层用的是Java语言，怎么才能让这两种语言进行交流呢，因为他们的编码方式是不一样的，这是就需要JNI了。
    - JNI可以被看作是代理模式，JNI是java接口，用于Java与C/C++之间的交互，作为两者的桥梁，也就是Java让JNI代其与C/C++沟通。
    - NDK是Android工具开发包，帮助快速开发C/C++动态库，相当于JDK开发java程序一样，同时能帮打包生成.so库


#### 1.3 JNI实践步骤
- 操作实践步骤
    - 第一步，编写native方法。
    - 第二步，根据此native方法编写C文件。
    - 第三步，使用NDK打包成.so库。
    - 第四步，使用.so库然后调用api。
- 如何使用NDK打包.so库
    - 1，编写Android.mk文件，此文件用来告知NDK打包.so库的规则
    - 2，使用ndk-build打包.so库
- 相关学习文档
    - NDK学习：https://developer.android.google.cn/ndk/guides?hl=zh-cn


#### 1.4 NDK使用场景
- NDK的使用场景一般在：
    - 1.为了提升这些模块的性能，对图形，视频，音频等计算密集型应用，将复杂模块计算封装在.so或者.a文件中处理。
    - 2.使用的是C/C++进行编写的第三方库移植。如ffmppeg，OpenGl等。
    - 3.某些情况下为了提高数据安全性，也会封装so来实现。毕竟使用纯Java开发的app是有很多逆向工具可以破解的。



### 02.NDK架构分层
- 使用NDK开发最终目标是为了将C/C++代码编译生成.so动态库或者静态库文件，并将库文件提供给Java代码调用。
- 所以按架构来分可以分为以下三层：
    - 1.构建层
    - 2.Java层
    - 3.native层


#### 2.1 NDK分层构建层
- 要得到目标的so文件，需要有个构建环境以及过程，将这个过程和环境称为构建层。
    - 构建层需要将C/C++代码编译为动态库so，那么这个编译的过程就需要一个构建工具，构建工具按照开发者指定的规则方式来构建库文件，类似apk的Gradle构建过程。
- 在讲解NDK构建工具之前，我们先来了解一些关于CPU架构的知识点：Android abi
    - ABI即Application Binary Interface，定义了二进制接口交互规则，以适应不同的CPU，一个ABI对应一种类型的CPU。
- Android目前支持以下7种ABI：
    - 1.armeabi：第5代和6代的ARM处理器，早期手机用的比较多。
    - 2.armeabi-v7a:第7代及以上的 ARM 处理器。
    - 3.arm64-v8a:第8代，64位ARM处理器
    - 4.x86:一般用在平板，模拟器。
    - 5.x86_64:64位平板。
- 常规的NDK构建工具有两种：
    - 1.ndk-build：
    - 2.Cmake
- ndk-build其实就是一个脚本。早期的NDK开发一直都是使用这种模式
    - 运行ndk-build相当于运行一下命令：$GNUMAKE -f <ndk>/build/core/build-local.mk
    - $GNUMAKE 指向 GNU Make 3.81 或更高版本，<ndk> 则指向 NDK 安装目录
    - 使用ndk-build需要配合两个mk文件：Android.mk和Application.mk。
- Cmake是一个编译系统的生成器
    - 简单理解就是，他是用来生成makefile文件的，Android.mk其实就是一个makefile类文件，cmake使用一个CmakeLists.txt的配置文件来生成对应的makefile文件。
    - Cmake构建so的过程其实包括两步：步骤1：使用Cmake生成编译的makefiles文件；步骤2：使用Make工具对步骤1中的makefiles文件进行编译为库或者可执行文件。
    - Cmake优势在哪里呢？在生成makefile过程中会自动分析源代码，创建一个组件之间依赖的关系树，这样就可以大大缩减在make编译阶段的时间。
- Cmake构建项目配置
    - 使用Cmake进行构建需要在build.gradle配置文件中声明externalNativeBuild


#### 2.2 NDK分层Java层
- 如何选择正确的so库呢
    - 通常情况下，我们在编译so的时候就需要确定自己设备类型，根据设备类型选择对应abiFilters。
    - 注意：使用as编译后的so会自动打包到apk中，如果需要提供给第三方使用，可以到build/intermediates/cmake/debug or release 目录中copy出来。
- Java层如何调用so文件中的函数
    - 对于Android上层代码来说，在将包正确导入到项目中后，只需要一行代码就可以完成动态库的加载过程。有两种方式：
    ```
    System.load("/data/local/tmp/native_lib.so"); 
    System.loadLibrary("native_lib");
    ```
    - 1.加载路径不同：load是加载so的完整路径，而loadLibrary是加载so的名称，然后加上前缀lib和后缀.so去默认目录下查找。
    - 2.自动加载库的依赖库的不同：load不会自动加载依赖库；而loadLibrary会自动加载依赖库。
- 无论哪种方式，最终都会调用到LoadNativeLibrary()方法，该方法主要操作：
    - 1.通过dlopen打开动态库文件
    - 2.通过dlsym找到JNI_OnLoad符号所对应的方法地址
    - 3.通过JNI_OnLoad去注册对应的jni方法



#### 2.3 Native层
- 如何理解JNI的设计思想
    - JNI（全名Java Native Interface）Java native接口，其可以让一个运行在Java虚拟机中的Java代码被调用或者调用native层的用C/C++编写的基于本机硬件和操作系统的程序。简单理解为就是一个连接Java层和Native层的桥梁。
    - 开发者可以在native层通过JNI调用到Java层的代码，也可以在Java层声明native方法的调用入口。
- JNI注册方式
    - 当Java代码中执行Native的代码的时候，首先是通过一定的方法来找到这些native方法。JNI有静态注册和动态注册两种注册方式。
    - 静态注册先由Java得到本地方法的声明，然后再通过JNI实现该声明方法。动态注册先通过JNI重载JNI_OnLoad()实现本地方法，然后直接在Java中调用本地方法。



### 03.JNI基础语法
#### 3.1 JNI三种引用
- 在JNI规范中定义了三种引用：
    - 局部引用（Local Reference）、全局引用（Global Reference）、弱全局引用（Weak Global Reference）。
- Local引用
    - JNI中使用 jobject, jclass, and jstring等来标志一个Java对象，然而在JNI方法在使用的过程中会创建很多引用类型，如果使用过程中不注意就会导致内存泄露。
    - 直接使用：NewLocalRef来创建。Local引用其实就是Java中的局部引用，在声明这个局部变量的方法结束或者退出其作用域后就会被GC回收。
- Global引用全局引用
    - 全局引用可以跨方法、跨线程使用，直到被开发者显式释放。一个全局引用在被释放前保证引用对象不被GC回收。
    - 和局部应用不同的是，能创建全局引用的函数只有NewGlobalRef，而释放它需要使用ReleaseGlobalRef函数。
- Weak引用
    - 弱引用可以使用全局声明的方式。弱引用在内存不足或者紧张的时候会自动回收掉，可能会出现短暂的内存泄露，但是不会出现内存溢出的情况。


#### 3.2 JNI异常处理
- native层异常
    - 处理方式1：native层自行处理
    - 处理方式2：native层抛出给Java层处理


#### 3.4 JNI核心原理



#### 3.5 注册Native函数
- JNI静态注册：
    - 步骤1.在Java中声明native方法，比如：public native String stringFromJNI()
    - 步骤2.在native层新建一个C/C++文件,并创建对应的方法(建议使用AS快捷键自动生成函数名)，比如：[testjnilib.cpp: Line 8](TestJniLib/src/main/cpp/testjnilib.cpp#L8-L8)
- JNI动态注册
    - 通过RegisterNatives方法把C/C++中的方法映射到Java中的native方法，而无需遵循特定的方法命名格式，这样书写起来会省事很多。
    - 动态注册其实就是使用到了前面分析的so加载原理：在最后一步的JNI_OnLoad中注册对应的jni方法。这样在类加载的过程中就可以自动注册native函数。比如：
    - 与JNI_OnLoad()函数相对应的有JNI_OnUnload()函数，当虚拟机释放该C库的时候，则会调用JNI_OnUnload()函数来进行善后清除工作。
- 那么如何选择使用静态注册or动态注册
    - 动态注册和静态注册最终都可以将native方法注册到虚拟机中，推荐使用动态注册，更不容易写错，静态注册每次增加一个新的方法都需要查看原函数类的包名。



### 04.一些必备操作
#### 4.1 so库生成打包
- 关于.so文件的生成有两种方式
    - 可以提供给大家参考，一种是CMake自动生成法，另一种是传统打包法。
- so文件在程序运行时就会加载
    - 所以想使用Java调用.so文件，必有某个Java类运行时load了native库，并通过JNI调用了它的方法。
- cmake生成.so方案
    - 第一步：创建native C++ Project项目，创建native函数并实现，先测试本地JNI函数调通
    - 第二步：获取.so文件。将生成的.apk文件改为.zip文件，然后进行解压缩，就能看到.so文件。如果想支持多种库架构，则可在module的build.gradle中配置ndk支持。
    - 第三步：so文件测试。新建一个普通的Android程序，将so库放入程序，然后创建类(注意要相同的包名、文件名及方法名)去加载so库。
    - 总结一下：Android Studio自动创建的native C++项目默认支持CMake方式，它支持JNI函数调用的入口在build.gradle中。
- 传统打包生成.so方案【不推荐这种方式】
    - 第一步：在Java类中声明一个本地方法。
    - 第二步：执行指令javah获得C声明的.h文件。
    - 第三步：获得.c文件并实现本地方法。创建Android.mk和Application.mk，并配置其参数，两个文件如不编写或编写正常会出现报错。
    - 第四步：打包.so库。cd到\app目录下，执行命令 ndk-build即可。生成so库后，最后测试ok即可。




### 05.实践几个案例
#### 5.1 Java调用C/C++
- Java调用C/C++函数调用流程
    - Java层调用某个函数时，会从对应的JNI层中寻找该函数。根据java函数的包名、方法名、参数列表等多方面来确定函数是否存在。
    - 如果没有就会报错，如果存在就会就会建立一个关联关系，以后再调用时会直接使用这个函数，这部分的操作由虚拟机完成。
- 举一个例子
    - 例如在 NativeLib 类的native stringFromJNI()方法，程序会自动在JNI层查找 Java_com_yc_testjnilib_NativeLib_stringFromJNI 函数接口，如未找到则报错。如找到，则会调用native库中的对应函数。


#### 5.2 C/C++调用Java
- Native层调用Java层的类的字段和方法的操作步骤
    - 第一步：创建一个Native C++的Android项目，创建 Native Lib 项目
    - 第二步：在cpp文件夹下创建：testjnilib.cpp文件，testjnilib.h文件(用来声明testjnilib.cpp中的方法)。
    - 第三步：开始编写配置文件CmkaeLists.txt文件。使用add_library创建一个新的so库
    - 第四步：编写 testjnilib.cpp文件。因为要实现native层调用Java层字段和方法，所以这里定义了两个方法：callJavaField和callJavaMethod
    - 第五步：编写Java层的调用代码此处要注意的是调用的类的类名以及包名都要和c++文件中声明的一致，否则会报错。具体看：NativeLib
    - 第六步：调用代码进行测试。然后查看测试结果


#### 5.3 Java调用so中API
- 




### 参考博客
- 入门级别jni开发教程
  - https://codeleading.com/article/10304397763/
- Android jni 开发详细流程
  - https://blog.csdn.net/weixin_39300133/article/details/90901837
- Android之jni开发流程【精品】
  - https://mp.weixin.qq.com/s/y-eKN-yb6chyoUrwNUG-8A
- Android JNI开发深度学习
  - https://blog.csdn.net/luo_boke/article/details/109455910

