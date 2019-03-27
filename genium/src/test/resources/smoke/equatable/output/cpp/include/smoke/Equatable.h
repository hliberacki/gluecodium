// -------------------------------------------------------------------------------------------------
//
//
// -------------------------------------------------------------------------------------------------
//
// Automatically generated. Do not modify. Your changes will be lost.
//
// -------------------------------------------------------------------------------------------------
#pragma once
#include "Export.h"
#include "Optional.h"
#include <cstdint>
#include <string>
#include <unordered_map>
#include <vector>
namespace smoke {
enum class SomeEnum {
    FOO,
    BAR
};
using ErrorCodeToMessageMap = ::std::unordered_map< int32_t, ::std::string >;
struct _GENIUM_CPP_EXPORT NestedEquatableStruct {
    ::std::string foo_field;
    NestedEquatableStruct( );
    NestedEquatableStruct( const ::std::string& foo_field );
    bool operator==( const NestedEquatableStruct& rhs ) const;
    bool operator!=( const NestedEquatableStruct& rhs ) const;
};
struct _GENIUM_CPP_EXPORT EquatableStruct {
    bool bool_field;
    int32_t int_field;
    int64_t long_field;
    float float_field;
    double double_field;
    ::std::string string_field;
    ::smoke::NestedEquatableStruct struct_field;
    ::smoke::SomeEnum enum_field;
    ::std::vector< ::std::string > array_field;
    ::smoke::ErrorCodeToMessageMap map_field;
    EquatableStruct( );
    EquatableStruct( const bool bool_field, const int32_t int_field, const int64_t long_field, const float float_field, const double double_field, const ::std::string& string_field, const ::smoke::NestedEquatableStruct& struct_field, const ::smoke::SomeEnum enum_field, const ::std::vector< ::std::string >& array_field, const ::smoke::ErrorCodeToMessageMap& map_field );
    bool operator==( const EquatableStruct& rhs ) const;
    bool operator!=( const EquatableStruct& rhs ) const;
};
struct _GENIUM_CPP_EXPORT EquatableNullableStruct {
    ::genium::optional< bool > bool_field;
    ::genium::optional< int32_t > int_field;
    ::genium::optional< uint16_t > uint_field;
    ::genium::optional< float > float_field;
    ::genium::optional< ::std::string > string_field;
    ::genium::optional< ::smoke::NestedEquatableStruct > struct_field;
    ::genium::optional< ::smoke::SomeEnum > enum_field;
    ::genium::optional< ::std::vector< ::std::string > > array_field;
    ::genium::optional< ::smoke::ErrorCodeToMessageMap > map_field;
    EquatableNullableStruct( );
    EquatableNullableStruct( const ::genium::optional< bool >& bool_field, const ::genium::optional< int32_t >& int_field, const ::genium::optional< uint16_t >& uint_field, const ::genium::optional< float >& float_field, const ::genium::optional< ::std::string >& string_field, const ::genium::optional< ::smoke::NestedEquatableStruct >& struct_field, const ::genium::optional< ::smoke::SomeEnum >& enum_field, const ::genium::optional< ::std::vector< ::std::string > >& array_field, const ::genium::optional< ::smoke::ErrorCodeToMessageMap >& map_field );
    bool operator==( const EquatableNullableStruct& rhs ) const;
    bool operator!=( const EquatableNullableStruct& rhs ) const;
};
}