# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("mooner_core")

add_library(
        mooner_core

        SHARED

        prevent_pthread_crash.c
        jni_init.c
        mooner_exception.c
        msponge.c
)
find_package(bytehook REQUIRED CONFIG)

target_link_libraries(mooner_core bytehook::bytehook)
find_package(shadowhook REQUIRED CONFIG)

target_link_libraries(mooner_core shadowhook::shadowhook)

find_library(
        log-lib

        log)


target_link_libraries(
        mooner_core

        ${log-lib})