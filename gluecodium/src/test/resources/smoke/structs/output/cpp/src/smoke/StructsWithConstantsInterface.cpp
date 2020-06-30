// -------------------------------------------------------------------------------------------------
//
//
// -------------------------------------------------------------------------------------------------
#include "smoke/StructsWithConstantsInterface.h"
#include <utility>
namespace smoke {
StructsWithConstantsInterface::StructsWithConstantsInterface() {
}
StructsWithConstantsInterface::~StructsWithConstantsInterface() {
}
const ::std::string StructsWithConstantsInterface::MultiRoute::DEFAULT_DESCRIPTION = "Foo";
const ::smoke::RouteType StructsWithConstantsInterface::MultiRoute::DEFAULT_TYPE = ::smoke::RouteType::NONE;
StructsWithConstantsInterface::MultiRoute::MultiRoute( )
    : descriptions{ }, type{ }
{
}
StructsWithConstantsInterface::MultiRoute::MultiRoute( ::std::vector< ::std::string > descriptions, ::smoke::RouteType type )
    : descriptions( std::move( descriptions ) ), type( std::move( type ) )
{
}
const ::std::string StructsWithConstantsInterface::StructWithConstantsOnly::DEFAULT_DESCRIPTION = "Foo";
}
