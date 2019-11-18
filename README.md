# AidlDemo
#AIDL的简单使用  
Server端：  
  1、创建AIDL文件  
  ![AIDL文件位置](images/AIDL文件位置.png)  
  2、在接口中定义方法  
  ![定义接口方法](images/Interface_AIDL.png)  
  3、构建Service服务  
  ![实现接口中定义的方法](images/implement_service.png)
  4、在manifest文件中注册服务  
  ![manifest](images/server_manifest.png)
 
 Client端：  
  1、将服务端AIDL文件拷贝到main文件下，包名需要与服务端一致。  
  ![Client端复制路径](images/client_aidl.png)
  2、build一下  
  3、连接绑定服务，获取AIDL接口实例  
  ![连接绑定](images/client_implement_aidl.png)
  4、调用接口  
  ![Client端调用接口](images/client_use_method.png)
