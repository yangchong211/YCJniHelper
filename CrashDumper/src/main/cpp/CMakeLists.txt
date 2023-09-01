cmake_minimum_required(VERSION 3.4.1)

add_library(
        crash_dumper

        SHARED

        crash_dumper.cpp
        stack_tracer.cpp
        readelf.cpp)


find_library(
        log-lib

        log)

target_link_libraries(
        crash_dumper

        ${log-lib})