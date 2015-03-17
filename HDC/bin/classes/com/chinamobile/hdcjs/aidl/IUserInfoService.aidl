package com.chinamobile.hdcjs.aidl;


interface IUserInfoService {
    // You can pass the value of in, out or inout
    // The primitive types (int, boolean, etc) are only passed by in
    
    void getToken(String packageName);

}