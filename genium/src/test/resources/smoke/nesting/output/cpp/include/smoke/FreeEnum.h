// -------------------------------------------------------------------------------------------------
//
//
// -------------------------------------------------------------------------------------------------
#pragma once
#include "gluecodium/Export.h"
#include "gluecodium/Hash.h"
#include <cstdint>
#include <system_error>
namespace smoke {
enum class FreeEnum {
    FOO,
    BAR
};
_GLUECODIUM_CPP_EXPORT ::std::error_code make_error_code( ::smoke::FreeEnum value ) noexcept;
}
namespace std
{
template <>
struct is_error_code_enum< ::smoke::FreeEnum > : public std::true_type { };
}
namespace gluecodium {
template<>
struct hash< ::smoke::FreeEnum > {
    _GLUECODIUM_CPP_EXPORT std::size_t operator( )( const ::smoke::FreeEnum& t ) const;
};
}