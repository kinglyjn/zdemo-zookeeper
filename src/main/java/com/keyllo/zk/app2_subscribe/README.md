
### 架构图

```

zk--						
    |config                     managerServer		
    |					
    |					
    |command                    controlServer
    |
    |					
    |servers
        |	
        |---server1         workServer1
        |                   workServer2
        |---server2         workServer3
        |					
        |---server3			

```
<br>


### 核心类

```

SubscribeZkClient---------->WorkServer---------------->ServerConfig
		|					 
		|
		|---------->ManagerServer------------>ServerData

```
