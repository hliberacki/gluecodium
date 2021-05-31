import 'dart:ffi';
import 'package:library/src/_library_context.dart' as __lib;
import 'package:library/src/_native_base.dart' as __lib;
import 'package:library/src/_token_cache.dart' as __lib;
import 'package:library/src/_type_repository.dart' as __lib;
import 'package:library/src/builtin_types__conversion.dart';
import 'package:library/src/smoke/parent_class.dart';
abstract class ChildClassFromClass implements ParentClass {
  /// Destroys the underlying native object.
  ///
  /// Call this to free memory when you no longer need this instance.
  /// Note that setting the instance to null will not destroy the underlying native object.
  void release();
  childClassMethod();
}
// ChildClassFromClass "private" section, not exported.
final _smokeChildclassfromclassCopyHandle = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_ChildClassFromClass_copy_handle'));
final _smokeChildclassfromclassReleaseHandle = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Void Function(Pointer<Void>),
    void Function(Pointer<Void>)
  >('library_smoke_ChildClassFromClass_release_handle'));
final _smokeChildclassfromclassGetTypeId = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_ChildClassFromClass_get_type_id'));
class ChildClassFromClass$Impl extends ParentClass$Impl implements ChildClassFromClass {
  ChildClassFromClass$Impl(Pointer<Void> handle) : super(handle);
  @override
  void release() {
    if (handle.address == 0) return;
    __lib.uncacheInstance(handle);
    _smokeChildclassfromclassReleaseHandle(handle);
    handle = Pointer<Void>.fromAddress(0);
  }
  @override
  childClassMethod() {
    final _childClassMethodFfi = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<Void Function(Pointer<Void>, Int32), void Function(Pointer<Void>, int)>('library_smoke_ChildClassFromClass_childClassMethod'));
    final _handle = this.handle;
    final __resultHandle = _childClassMethodFfi(_handle, __lib.LibraryContext.isolateId);
    try {
      return (__resultHandle);
    } finally {
    }
  }
}
Pointer<Void> smokeChildclassfromclassToFfi(ChildClassFromClass value) =>
  _smokeChildclassfromclassCopyHandle((value as __lib.NativeBase).handle);
ChildClassFromClass smokeChildclassfromclassFromFfi(Pointer<Void> handle) {
  final instance = __lib.getCachedInstance(handle);
  if (instance != null && instance is ChildClassFromClass) return instance as ChildClassFromClass;
  final _typeIdHandle = _smokeChildclassfromclassGetTypeId(handle);
  final factoryConstructor = __lib.typeRepository[stringFromFfi(_typeIdHandle)];
  stringReleaseFfiHandle(_typeIdHandle);
  final _copiedHandle = _smokeChildclassfromclassCopyHandle(handle);
  final result = factoryConstructor != null
    ? factoryConstructor(_copiedHandle)
    : ChildClassFromClass$Impl(_copiedHandle);
  __lib.cacheInstance(_copiedHandle, result);
  return result;
}
void smokeChildclassfromclassReleaseFfiHandle(Pointer<Void> handle) =>
  _smokeChildclassfromclassReleaseHandle(handle);
Pointer<Void> smokeChildclassfromclassToFfiNullable(ChildClassFromClass? value) =>
  value != null ? smokeChildclassfromclassToFfi(value) : Pointer<Void>.fromAddress(0);
ChildClassFromClass? smokeChildclassfromclassFromFfiNullable(Pointer<Void> handle) =>
  handle.address != 0 ? smokeChildclassfromclassFromFfi(handle) : null;
void smokeChildclassfromclassReleaseFfiHandleNullable(Pointer<Void> handle) =>
  _smokeChildclassfromclassReleaseHandle(handle);
// End of ChildClassFromClass "private" section.
