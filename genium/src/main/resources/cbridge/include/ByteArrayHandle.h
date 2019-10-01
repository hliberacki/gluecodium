// -------------------------------------------------------------------------------------------------
// Copyright (C) 2016-2019 HERE Europe B.V.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// SPDX-License-Identifier: Apache-2.0
// License-Filename: LICENSE
//
// -------------------------------------------------------------------------------------------------

#pragma once

#include "BaseHandle.h"
#include "Export.h"
#include <stddef.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

_GLUECODIUM_C_EXPORT _baseRef byteArray_create_handle( );
_GLUECODIUM_C_EXPORT void byteArray_release_handle( _baseRef handle );
_GLUECODIUM_C_EXPORT void byteArray_assign( _baseRef handle, const uint8_t* data, const size_t size );
_GLUECODIUM_C_EXPORT const uint8_t* byteArray_data_get( _baseRef handle );
_GLUECODIUM_C_EXPORT size_t byteArray_size_get( _baseRef handle );

_GLUECODIUM_C_EXPORT _baseRef byteArray_create_optional_handle( );
_GLUECODIUM_C_EXPORT void byteArray_release_optional_handle( _baseRef handle );
_GLUECODIUM_C_EXPORT _baseRef byteArray_unwrap_optional_handle( _baseRef handle );
_GLUECODIUM_C_EXPORT void byteArray_assign_optional( _baseRef handle, const uint8_t* data, const size_t size );

#ifdef __cplusplus
}
#endif
