#### 目录介绍
- 01.学习JNI开发流程
    - 1.1 JNI和NDK的关系
    - 1.2 JNI开发流程
    - 1.3 JNI实践步骤
    - 1.4 NDK使用场景
- 02.NDK架构分层
    - 2.1 NDK分层构建层
    - 2.2 NDK分层Java层
    - 2.3 Native层




### 01.学习JNI开发流程
#### 1.1 JNI和NDK的关系
- JNI和NDK学习内容太难
    - 其实难的不是JNI和NDK，而是C/C++语言，JNI和NDK只是个工具，很容易学习的。
- JNI和NDK有何联系
    - 学习JNI之前，首先得先知道JNI、NDK、Java和C/C++之间的关系。
    - 在Android开发中，有时为了性能和安全性（反编译），需要使用C/C++语言，但是Android APP层用的是Java语言，怎么才能让这两种语言进行交流呢，因为他们的编码方式是不一样的，这是就需要JNI了。
    - JNI可以被看作是代理模式，Java使用JVM加载并调用JNI来间接调用C/C++代码，也就是Java让JNI代其与C/C++沟通。
    - NDK呢其实主要就是用来将C/C++代码打包编译成.so库的。


#### 1.2 JNI开发流程
- JNI开发流程主要分为以下6步：
    - 1、编写声明了native方法的Java类
    - 2、将Java源代码编译成class字节码文件
    - 3、用javah -jni命令生成.h头文件（javah是jdk自带的一个命令，-jni参数表示将class中用native声明的函数生成jni规则的函数）
    - 4、用本地代码实现.h头文件中的函数
    - 5、将本地代码编译成动态库（windows：*.dll，linux/unix：*.so，mac os x：*.jnilib）
    - 6、拷贝动态库至 java.library.path 本地库搜索目录下，并运行Java程序


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
    ```
    android {
        defaultConfig {
            externalNativeBuild {
                cmake {
                    //声明当前Cmake项目使用的Android abi
                    abiFilters "armeabi-v7a"
                    //提供给Cmake的参数信息 可选
                    arguments "-DANDROID_ARM_NEON=TRUE", "-DANDROID_TOOLCHAIN=clang"
                    //提供给C编译器的一个标志 可选
                    cFlags "-D__STDC_FORMAT_MACROS"
                    //提供给C++编译器的一个标志 可选
                    cppFlags "-fexceptions", "-frtti","-std=c++11"
                    //指定哪个so库会被打包到apk中去 可选
                    targets "libexample-one", "my-executible-demo"
                }
            }
        }
        externalNativeBuild {
            cmake {
                path "src/main/cpp/CMakeLists.txt" //声明cmake配置文件路径
                version "3.10.2" //声明cmake版本
            }
    
        }
    }
    ```



#### 2.2 NDK分层Java层




#### 2.3 Native层







### 参考博客
- 入门级别jni开发教程
  - https://codeleading.com/article/10304397763/
- Android jni 开发详细流程
  - https://blog.csdn.net/weixin_39300133/article/details/90901837
- Android之jni开发流程
  - https://mp.weixin.qq.com/s/y-eKN-yb6chyoUrwNUG-8A
- Android JNI开发深度学习
  - https://blog.csdn.net/luo_boke/article/details/109455910

