// -------------------------------------------------------------------------------------------------
//
//
// -------------------------------------------------------------------------------------------------
#include "smoke/SimpleInterface.h"
namespace gluecodium {
TypeRepository& get_type_repository(const ::smoke::SimpleInterface*) {
    static TypeRepository s_repo;
    return s_repo;
}
}
namespace smoke {
SimpleInterface::SimpleInterface() {
}
SimpleInterface::~SimpleInterface() {
}
}