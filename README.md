# AidlDemo
## AIDL的简单使用  
### Server端：  
  1、创建AIDL文件  
  ![AIDL文件位置](images/AIDL文件位置.png)  
  2、在接口中定义方法  
  ![定义接口方法](images/Interface_AIDL.png)    
  3、构建Service服务  
  ![实现接口中定义的方法](images/implement_service.png)  
  4、在manifest文件中注册服务  
  ![manifest](images/server_manifest.png)  
 
 ### Client端：  
  1、将服务端AIDL文件拷贝到main文件下，包名需要与服务端一致。  
  ![Client端复制路径](images/client_aidl.png)  
  2、build一下  
  3、连接绑定服务，获取AIDL接口实例  
  ![连接绑定](images/client_implement_aidl.png)  
  4、调用接口  
  ![Client端调用接口](images/client_use_method.png)  


### 运行结果：  
![成功显示](images/result.png)  


### 使用了JavaBean的情况  
1、创建一个JavaBean实体类，并且继承Parcelable  
![javabean](images/server_bean_entity.png)  
2、在对应的AIDL包下创建对应实体类的AIDL文件，并且把它改成声明为parcelable数据类型的AIDL文件  
![server_bean_aid](images/server_bean_aidl.png)  
3、还是将所有的AIDL文件拷贝的Client端的对应目录下，还是要包名相同  
![client_copy](images/client_copy.png)  
4、注意要将JavaBean文件也拷贝过去  
5、增加了一个新的方法来测试  
![client_new_use](images/client_new_use.png)  

### 运行结果  
![](images/client_new_result.png)  
