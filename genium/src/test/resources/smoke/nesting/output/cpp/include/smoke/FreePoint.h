// -------------------------------------------------------------------------------------------------
//
//
// -------------------------------------------------------------------------------------------------
#pragma once
#include "gluecodium/Export.h"
#include "smoke/FreeEnum.h"
namespace smoke {
struct _GLUECODIUM_CPP_EXPORT FreePoint {
    double x;
    double y;
    FreePoint( );
    FreePoint( const double x, const double y );
    ::smoke::FreePoint flip(  ) const;
    _GLUECODIUM_CPP_EXPORT static const ::smoke::FreeEnum A_BAR;
};
}
