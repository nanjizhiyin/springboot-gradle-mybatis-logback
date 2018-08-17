整个项目分两部分.
1.一个项目synctool用于同步mongodb到本地mysql
2.给前端提供接口的项目syncapi

接口:
1.修改配置
http://10.10.1.169:8090/conf/saveconf
参数:
     file:硬盘比例
     cpu:cpu比例
     mem:内存比例
     executionTime:每天的执行时间
     forwardTime:删除N天前的数据
