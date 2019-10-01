/*
 *
 */
#pragma once
#include "CppProxyBase.h"
#include "FieldAccessMethods.h"
#include "JniBase.h"
#include "JniCppConversionUtils.h"
#include "foo/Bar.h"
#include <jni.h>
#include <memory>
#include "gluecodium/Optional.h"
namespace gluecodium
{
namespace jni
{
::foo::BarEnum convert_from_jni(JNIEnv* _jenv, const JniReference<jobject>& _jinput, ::foo::BarEnum* dummy);
::gluecodium::optional<::foo::BarEnum> convert_from_jni(JNIEnv* _jenv, const JniReference<jobject>& _jinput, ::gluecodium::optional<::foo::BarEnum>* dummy);
JniReference<jobject> convert_to_jni(JNIEnv* _jenv, const ::foo::BarEnum _ninput);
JniReference<jobject> convert_to_jni(JNIEnv* _jenv, const ::gluecodium::optional<::foo::BarEnum> _ninput);
}
}
