/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\eclipse\\workspace\\HDC\\src\\com\\chinamobile\\hdcjs\\aidl\\IUserInfoService.aidl
 */
package com.chinamobile.hdcjs.aidl;
public interface IUserInfoService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.chinamobile.hdcjs.aidl.IUserInfoService
{
private static final java.lang.String DESCRIPTOR = "com.chinamobile.hdcjs.aidl.IUserInfoService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.chinamobile.hdcjs.aidl.IUserInfoService interface,
 * generating a proxy if needed.
 */
public static com.chinamobile.hdcjs.aidl.IUserInfoService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.chinamobile.hdcjs.aidl.IUserInfoService))) {
return ((com.chinamobile.hdcjs.aidl.IUserInfoService)iin);
}
return new com.chinamobile.hdcjs.aidl.IUserInfoService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getToken:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.getToken(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.chinamobile.hdcjs.aidl.IUserInfoService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
// You can pass the value of in, out or inout
// The primitive types (int, boolean, etc) are only passed by in

@Override public void getToken(java.lang.String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_getToken, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
// You can pass the value of in, out or inout
// The primitive types (int, boolean, etc) are only passed by in

public void getToken(java.lang.String packageName) throws android.os.RemoteException;
}
