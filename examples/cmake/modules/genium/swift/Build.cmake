# Copyright (C) 2016-2018 HERE Europe B.V.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# SPDX-License-Identifier: Apache-2.0
# License-Filename: LICENSE

if(DEFINED includeguard_genium_swift_Build)
  return()
endif()
set(includeguard_genium_swift_Build ON)

cmake_minimum_required(VERSION 3.5)

include(${CMAKE_CURRENT_LIST_DIR}/Compile.cmake)
include(${CMAKE_CURRENT_LIST_DIR}/Modulemap.cmake)
include(${CMAKE_CURRENT_LIST_DIR}/Configuration.cmake)

#.rst:
# apigen_swift_build
# -------------------
#
# This module build the framework binary
# a swift framework.
#
# .. command:: apigen_swift_build
#
# The general form of the command is::
#
#     apigen_swift_build(target
#       MODULEMAP_HEADERS file_list)
#
# MODULEMAP_HEADERS specifies additional file list to put into generated modulemap file
#

function(apigen_swift_build target)

  set (multiArgs MODULEMAP_HEADERS)

  cmake_parse_arguments(APIGEN_SWIFT_BUILD "" "${singleArgs}" "${multiArgs}" ${ARGN})

  get_target_property(GENERATOR ${target} APIGEN_GENIUM_GENERATOR)

  if(NOT ${GENERATOR} MATCHES "swift")
    message(FATAL_ERROR "apigen_swift_build() depends on apigen_generate() configured with generator 'swift'")
  endif()

  apigen_swift_configuration(${target})
  apigen_swift_modulemap(${target} HEADERS ${APIGEN_SWIFT_BUILD_MODULEMAP_HEADERS})

  if(CMAKE_CROSSCOMPILING)
    foreach(TARGET_ARCH IN LISTS CMAKE_OSX_ARCHITECTURES)
      message(STATUS "[Swift] COMPILING ${target} ${TARGET_ARCH}")
      apigen_swift_compile(${target} "${TARGET_ARCH}")
    endforeach()

    set_target_properties(${target} PROPERTIES APIGEN_SWIFT_BUILD_ARCH "${CMAKE_OSX_ARCHITECTURES}")
    message(STATUS "[Swift] FAT ${target} ${CMAKE_OSX_ARCHITECTURES}")
  else()
    message(STATUS "[Swift] COMPILING ${target} ${CMAKE_SYSTEM_PROCESSOR}")
    apigen_swift_compile(${target} ${CMAKE_SYSTEM_PROCESSOR})
    set_target_properties(${target} PROPERTIES APIGEN_SWIFT_BUILD_ARCH ${CMAKE_SYSTEM_PROCESSOR})
  endif()

endfunction()
