#### 目录介绍
- 01.学习JNI开发流程
    - 1.1 JNI开发概念
    - 1.2 JNI和NDK的关系
    - 1.3 JNI实践步骤
    - 1.4 NDK使用场景
    - 1.5 学习路线说明
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
    - 3.6 JNI签名是什么
- 04.一些必备操作
    - 4.1 so库生成打包
    - 4.2 so库查询操作
    - 4.3 so库如何反编译
- 05.实践几个案例
    - 5.1 Java静态调用C/C++
    - 5.2 C/C++调用Java
    - 5.3 Java调三方so中API
    - 5.4 Java动态调C++
- 06.一些技术原理
    - 6.1 JNIEnv创建和释放
    - 6.2 动态注册的原理
    - 6.3 注册JNI流程图
- 07.JNI遇到的问题
    - 7.1 混淆的bug
    - 7.2 注意字符串编译



### 01.学习JNI开发流程
#### 1.1 JNI开发概念
- .SO库是什么东西
    - NDK为了方便使用，提供了一些脚本，使得更容易的编译C/C++代码。在Android程序编译中会将C/C++ 编译成动态库 so 文件，类似java库.jar文件一样，它的生成需要使用NDK工具来打包。
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



#### 1.5 学习路线说明
- JNI学习路线介绍
    - 1.首先要有点C/C++的基础，这个我是在 [菜鸟教程](https://www.runoob.com/cplusplus/cpp-tutorial.html) 上学习的
    - 2.理解NDK和JNI的一些概念，以及NDK的一个大概的架构分层，JNI的开发步骤是怎样的
    - 3.掌握案例练习，前期先写案例，比如java调用c/c++，或者c/c++调用java。把这个案例写熟，跑通即可
    - 4.案例练习之后，然后在思考NDK是怎么编译的，如何打包so文件，loadLibrary的流程，CMake工作流程等一些基础的原理
    - 5.在实践过程中，先记录遇到的问题。这时候可能不一定懂，先放着，先实现案例或者简单的业务。然后边实践边琢磨问题和背后的原理
- 注意事项介绍
    - 避免一开始就研究原理，或者把C/C++整体学习一遍，那样会比较辛苦。焦点先放在JNI通信流程上，写案例学习
    - 把学习内容，分为几个不同类型：了解(能够扯淡)，理解(大概知道什么意思)，掌握(能够运用和实践)，精通(能举一反三和分享讲清楚)


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
    - 3.arm64-v8a:第8代，64位ARM处理器。
    - 4.x86:一般用在平板，模拟器。
    - 5.x86_64:64位平板。
- 常规的NDK构建工具有两种：
    - 1.ndk-build：
    - 2.Cmake
- ndk-build其实就是一个脚本。早期的NDK开发一直都是使用这种模式
    - 运行ndk-build相当于运行一下命令：$GNUMAKE -f <ndk>/build/core/build-local.mk
    - $GNUMAKE 指向 GNU Make 3.81 或更高版本，<ndk> 则指向 NDK 安装目录
    - 使用ndk-build需要配合两个mk文件：Android.mk 和 Application.mk。
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
- java运行在jvm，jvm本身就是使用C/C++编写的，因此jni只需要在java代码、jvm、C/C++代码之间做切换即可
    - ![image](https://img-blog.csdnimg.cn/331badb23b4e44cda0ced26c6dcf9478.png)
- JNIEnv是什么？
    - JINEnv是当前Java线程的执行环境，一个JVM对应一个JavaVM结构体，一个JVM中可能创建多个Java线程，每个线程对应一个JNIEnv结构，它们保存在线程本地存储TLS中。
    - 因此不同的线程JNIEnv不同，而不能相互共享使用。 JavaEnv结构也是一个函数表，在本地代码通过JNIEnv函数表来操作Java数据或者调用Java方法。



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



#### 3.6 JNI签名是什么
- 为什么JNI中突然多出了一个概念叫”签名”：
    - 因为Java是支持函数重载的，也就是说，可以定义相同方法名，但是不同参数的方法，然后Java根据其不同的参数，找到其对应的实现的方法。
    - 这样是很好，所以说JNI肯定要支持的，如果仅仅是根据函数名，没有办法找到重载的函数的，所以为了解决这个问题，JNI就衍生了一个概念——”签名”，即将参数类型和返回值类型的组合。
    - 如果拥有一个该函数的签名信息和这个函数的函数名，就可以顺序的找到对应的Java层中的函数。
- 如何查看签名呢：可以使用javap命令。
    - javap -s -p MainActivity.class



### 04.一些必备操作
#### 4.1 so库生成打包
- 什么是so文件库
    - so库，即将C或者C++实现的功能进行打包，将其打包为共享库，让其他程序进行调用，这可以提高代码的复用性。
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



#### 4.2 so库查询操作
- so库如何查找所对应的位置
    - 第一步：在 app 模块的 build.gradle 中，追加以下代码：
    - 第二步：执行命令行：./gradlew assembleDebug 【注意如果遇到gradlew找不到，则输入：chmod +x gradlew】
- so文件查询结果后。就可以查询到so文件属于那个lib库的！如下所示：libtestjnilib.so文件属于TestJniLib库的
    ```
    find so file: /Users/yc/github/YCJniHelper/TestJniLib/build/intermediates/library_jni/debug/jni/armeabi-v7a/libtestjnilib.so
    find so file: /Users/yc/github/YCJniHelper/SafetyJniLib/build/intermediates/library_jni/debug/jni/armeabi-v7a/libsafetyjnilib.so
    find so file: /Users/yc/github/YCJniHelper/SignalHooker/build/intermediates/library_jni/debug/jni/armeabi-v7a/libsignal-hooker.so
    ```


### 05.实践几个案例
#### 5.1 Java静态调用C/C++
- Java调用C/C++函数调用流程
    - Java层调用某个函数时，会从对应的JNI层中寻找该函数。根据java函数的包名、方法名、参数列表等多方面来确定函数是否存在。
    - 如果没有就会报错，如果存在就会就会建立一个关联关系，以后再调用时会直接使用这个函数，这部分的操作由虚拟机完成。
- Java层调用C/C++方法操作步骤
    - 第一步：创建java类NativeLib，然后定义native方法stringFromJNI()
    ```
    public native String stringFromJNI();
    ```
    - 第二步：根据此native方法编写C文件，可以通过命令后或者studio提示生成C++对应的方法函数
    ```
    //java中stringFromJNI
    //extern “C”    指定以"C"的方式来实现native函数
    extern "C"
    //JNIEXPORT     宏定义，用于指定该函数是JNI函数。表示此函数可以被外部调用，在Android开发中不可省略
    JNIEXPORT jstring
    //JNICALL       宏定义，用于指定该函数是JNI函数。，无实际意义，但是不可省略
    JNICALL
    //以注意到jni的取名规则，一般都是包名 + 类名，jni方法只是在前面加上了Java_，并把包名和类名之间的.换成了_
    Java_com_yc_testjnilib_NativeLib_stringFromJNI(JNIEnv *env, jobject /* this */) {
        //JNIEnv 代表了JNI的环境，只要在本地代码中拿到了JNIEnv和jobject
        //JNI层实现的方法都是通过JNIEnv 指针调用JNI层的方法访问Java虚拟机，进而操作Java对象，这样就能调用Java代码。
        //jobject thiz
        //在AS中自动为我们生成的JNI方法声明都会带一个这样的参数，这个instance就代表Java中native方法声明所在的
        std::string hello = "Hello from C++";
        
        //思考一下，为什么直接返回字符串会出现错误提示？
        //return "hello";
        return env->NewStringUTF(hello.c_str());
    }
    ```
- 举一个例子
    - 例如在 NativeLib 类的native stringFromJNI()方法，程序会自动在JNI层查找 Java_com_yc_testjnilib_NativeLib_stringFromJNI 函数接口，如未找到则报错。如找到，则会调用native库中的对应函数。


#### 5.2 C/C++调用Java
- Native层调用Java层的类的字段和方法的操作步骤
    - 第一步：创建一个Native C++的Android项目，创建 Native Lib 项目
    - 第二步：在cpp文件夹下创建：calljnilib.cpp文件，calljnilib.h文件(用来声明calljnilib.cpp中的方法)。
    - 第三步：开始编写配置文件CmkaeLists.txt文件。使用add_library创建一个新的so库
    - 第四步：编写 calljnilib.cpp文件。因为要实现native层调用Java层字段和方法，所以这里定义了两个方法：callJavaField和callJavaMethod
    - 第五步：编写Java层的调用代码此处要注意的是调用的类的类名以及包名都要和c++文件中声明的一致，否则会报错。具体看：CallNativeLib
    - 第六步：调用代码进行测试。然后查看测试结果


#### 5.3 Java调三方so中API
- 直接拿前面案例的 calljnilib.so 来测试，但是为了实现三方调用还需要对文件进行改造
    - 第一步：要实现三方so库调用，在 calljnilib.h中声明两个和 calljnilib.cpp中对应的方法：callJavaField和callJavaMethod，一般情况下这个头文件是第三方库一起提供的给外部调用的。
    - 第二步：对CMakeLists配置文件改造。主要是做一些库的配置操作。
    - 第三步：编写 third_call.cpp文件，在这内部调用第三方库。这里需要将第三方头文件导入进来，如果CmakeLists文件中没有声明头文件，就使用#include "include/calljnilib.h" 这种方式导入
    - 第四步：最后测试下：callThirdSoMethod("com/yc/testjnilib/HelloCallBack","updateName");



#### 5.4 Java动态调C++
- 先说一下静态调C++的问题：
    - 在实现stringFromJNI()时，可以看到c++里面的方法名很长 Java_com_yc_testjnilib_NativeLib_stringFromJNI。
    - 这是jni静态注册的方式，按照jni规范的命名规则进行查找，格式为Java_类路径_方法名。Studio默认这种方式名字太长了，能否设置短一点。
    - 程序运行效率低，因为初次调用native函数时需要根据根据函数名在JNI层中搜索对应的本地函数，然后建立对应关系，这个过程比较耗时。
- 动态注册方法解决上面问题
    - 当程序在Java层运行System.loadLibrary("testjnilib")；这行代码后，程序会去载入testjnilib.so文件。
    - 于此同时，产生一个Load事件，这个事件触发后，程序默认会在载入的.so文件的函数列表中查找JNI_OnLoad函数并执行。与Load事件相对，在载入的.so文件被卸载时，Unload事件被触发。
    - 此时，程序默认会去载入的.so文件的函数列表中查找JNI_OnLoad函数并执行，然后卸载.so文件。
    - 因此开发者经常会在JNI_OnLoad中做一些初始化操作，动态注册就是在这里进行的，使用env->RegisterNatives(clazz, gMethods, numMethods)。
- 动态注册操作步骤：
    - 第一步：因为System.loadLibrary()执行时会调用此方法，实现JNI_OnLoad方法。
    - 第二步：调用FindClass找到需要动态注册的java类【定义要关联的对应Java类】，注意这个是native方法那个类的路径字符串
    - 第三步：定义一个静态数据(JNINativeMethod类型)，里面存放需要动态注册的native方法，以及参数名称
    - 第四步：通过调用jni中的RegisterNatives函数将注册函数的Java类，以及注册函数的数组，以及个数注册在一起，这样就实现了绑定。
- 动态注册优势分析
    - 相比静态注册，动态注册的灵活性更高，如果修改了native函数所在类的包名或类名，仅调整native函数的签名信息即可。
    - 还有一个优势：动态注册，java代码不需要更改，只需要更改native代码。
    - 效率更高：通过在.so文件载入初始化时，即JNI_OnLoad函数中，先行将native函数注册到VM的native函数链表中去，后续每次java调用native函数时都会在VM中的native函数链表中找到对应的函数，从而加快速度。



### 06.一些技术原理
#### 6.1 JNIEnv创建和释放
- JNIEnv的创建方式
    - C 中——JNIInvokeInterface：JNIInvokeInterface是C语言环境中的JavaVM结构体，调用 (AttachCurrentThread)(JavaVM, JNIEnv*, void) 方法，能够获得JNIEnv结构体；
    - C++中 ——_JavaVM：_JavaVM是C++中JavaVM结构体，调用jint AttachCurrentThread(JNIEnv** p_env, void* thr_args) 方法，能够获取JNIEnv结构体；
- JNIEnv的释放：
    - C 中释放：调用JavaVM结构体JNIInvokeInterface中的(DetachCurrentThread)(JavaVM)方法，能够释放本线程的JNIEnv
    - C++ 中释放：调用JavaVM结构体_JavaVM中的jint DetachCurrentThread(){ return functions->DetachCurrentThread(this); } 方法，就可以释放 本线程的JNIEnv
- JNIEnv和线程的关系
    - JNIEnv只在当前线程有效：JNIEnv仅仅在当前线程有效，JNIEnv不能在线程之间进行传递，在同一个线程中，多次调用JNI层方便，传入的JNIEnv是同样的
    - 本地方法匹配多个JNIEnv：在Java层定义的本地方法，能够在不同的线程调用，因此能够接受不同的JNIEnv



#### 6.2 动态注册的原理
- 在Android源码开发环境下，大多采用动态注册native方法。
    - 利用结构体JNINativeMethod保存Java Native函数和JNI函数的对应关系；
    - 在一个JNINativeMethod数组中保存所有native函数和JNI函数的对应关系；
    - 在Java中通过System.loadLibrary加载完JNI动态库之后，调用JNI_OnLoad函数，开始动态注册；
    - JNI_OnLoad中会调用AndroidRuntime::registerNativeMethods函数进行函数注册；
    - AndroidRuntime::registerNativeMethods中最终调用jni RegisterNativeMethods完成注册。
- 动态注册原理分析
    - RegisterNatives 方式的本质是直接通过结构体指定映射关系，而不是等到调用 native 方法时搜索 JNI 函数指针，因此动态注册的 native 方法调用效率更高。
    - 此外，还能减少生成 so 库文件中导出符号的数量，则能够优化 so 库文件的体积。



#### 6.3 注册JNI流程图
- 提到了注册 JNI 函数（建立 Java native 方法和 JNI 函数的映射关系）有两种方式：静态注册和动态注册。
    - ![image](https://img-blog.csdnimg.cn/80fba6af41d749e384f63c8d0577d530.png)
- 分析下静态注册匹配 JNI 函数的执行过程
    - 第一步：以 loadLibrary() 加载 so 库的执行流程为线索进行分析的，最终定位到 FindNativeMethod() 这个方法。
    - 第二步：查看`java_vm_ext.cc`中FindNativeMethod方法，然后看到jni_short_name和jni_long_name，获取native方法对应的短名称和长名称。
    - 第三步：在`java_vm_ext.cc`，通过FindNativeMethodInternal查找已经加载的so库中搜索，先搜索短名称，然后再搜索长名称
    - 第四步：建立内部数据结构，建立 Java native 方法与 JNI 函数的函数指针的映射关系，调用 native 方法，则直接调用已记录的函数指针。




### 07.JNI遇到的问题
#### 7.1 混淆的bug
- 在Android工程中要排除对native方法以及所在类的混淆（java工程不需要），否则要注册的java类和java函数会找不到。proguard-rules.pro中添加。
    ```
    # 设置所有 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }
    # 不混淆类
    -keep class com.yc.testjnilib.** { *; }
    ```


#### 7.2 注意字符串编译
- 比如：对于JNI方法来说，使用如下方法返回或者调用直接崩溃了，有点搞不懂原理？
    ```
    env->CallMethod(objCallBack,_methodName,"123");
    ```
- 这段代码编译没问题，但是在运行的时候就报错了：
    ```
    JNI DETECTED ERROR IN APPLICATION: use of deleted global reference
    ```
- 最终定位到是最后一个参数需要使用jstring而不能直接使用字符串表示。如下所示：
    ```
    //思考一下，为什么直接返回字符串会出现错误提示？为何这样设计……
    //return "hello";
    return env->NewStringUTF(hello.c_str());
    ```



### 代码案例：https://github.com/yangchong211/YCJniHelper
### 其他案例：https://github.com/yangchong211/YCAppTool














