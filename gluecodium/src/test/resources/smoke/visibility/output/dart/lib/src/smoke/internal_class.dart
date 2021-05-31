import 'dart:ffi';
import 'package:library/src/_library_context.dart' as __lib;
import 'package:library/src/_native_base.dart' as __lib;
import 'package:library/src/_token_cache.dart' as __lib;
/// @nodoc
abstract class InternalClass {
  /// Destroys the underlying native object.
  ///
  /// Call this to free memory when you no longer need this instance.
  /// Note that setting the instance to null will not destroy the underlying native object.
  void release();
  /// @nodoc
  internal_fooBar();
}
// InternalClass "private" section, not exported.
final _smokeInternalclassCopyHandle = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_InternalClass_copy_handle'));
final _smokeInternalclassReleaseHandle = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Void Function(Pointer<Void>),
    void Function(Pointer<Void>)
  >('library_smoke_InternalClass_release_handle'));
class InternalClass$Impl extends __lib.NativeBase implements InternalClass {
  InternalClass$Impl(Pointer<Void> handle) : super(handle);
  @override
  void release() {
    if (handle.address == 0) return;
    __lib.uncacheInstance(handle);
    _smokeInternalclassReleaseHandle(handle);
    handle = Pointer<Void>.fromAddress(0);
  }
  @override
  internal_fooBar() {
    final _fooBarFfi = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<Void Function(Pointer<Void>, Int32), void Function(Pointer<Void>, int)>('library_smoke_InternalClass_fooBar'));
    final _handle = this.handle;
    final __resultHandle = _fooBarFfi(_handle, __lib.LibraryContext.isolateId);
    try {
      return (__resultHandle);
    } finally {
    }
  }
}
Pointer<Void> smokeInternalclassToFfi(InternalClass value) =>
  _smokeInternalclassCopyHandle((value as __lib.NativeBase).handle);
InternalClass smokeInternalclassFromFfi(Pointer<Void> handle) {
  final instance = __lib.getCachedInstance(handle);
  if (instance != null && instance is InternalClass) return instance as InternalClass;
  final _copiedHandle = _smokeInternalclassCopyHandle(handle);
  final result = InternalClass$Impl(_copiedHandle);
  __lib.cacheInstance(_copiedHandle, result);
  return result;
}
void smokeInternalclassReleaseFfiHandle(Pointer<Void> handle) =>
  _smokeInternalclassReleaseHandle(handle);
Pointer<Void> smokeInternalclassToFfiNullable(InternalClass? value) =>
  value != null ? smokeInternalclassToFfi(value) : Pointer<Void>.fromAddress(0);
InternalClass? smokeInternalclassFromFfiNullable(Pointer<Void> handle) =>
  handle.address != 0 ? smokeInternalclassFromFfi(handle) : null;
void smokeInternalclassReleaseFfiHandleNullable(Pointer<Void> handle) =>
  _smokeInternalclassReleaseHandle(handle);
// End of InternalClass "private" section.
