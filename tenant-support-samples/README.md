# tenant-support 例子

## 快速初始化

### 1. 初始化数据库

这里使用 MYSQL 进行演示,可以使用 docker 快速搭建 MySQL 环境

```shell
> docker pull mysql:latest


latest: Pulling from library/mysql
b4d181a07f80: Pull complete 
a462b60610f5: Pull complete 
578fafb77ab8: Pull complete 
524046006037: Pull complete 
d0cbe54c8855: Pull complete 
aa18e05cc46d: Pull complete 
32ca814c833f: Pull complete 
9ecc8abdb7f5: Pull complete 
ad042b682e0f: Pull complete 
71d327c6bb78: Pull complete 
165d1d10a3fa: Pull complete 
2f40c47d0626: Pull complete 
Digest: sha256:52b8406e4c32b8cf0557f1b74517e14c5393aff5cf0384eff62d9e81f4985d4b
Status: Downloaded newer image for mysql:latest
docker.io/library/mysql:latest

```

### 1.1 启动主租户的 MySQL实例:

````shell
docker run -itd --name mysql-master-tenant-support-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql

````

连接到该数据库实例上,执行 tenant-support/src/sql/tenant_master_init.sql 文件

### 1.2 启动测试租户2 的 MySQL实例 :

````shell
docker run -itd --name mysql-tenant-2-support-test -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql

````

连接到租户2数据库实例上,执行 tenant-support/src/sql/tenant_2_init.sql 文件

### 1.3 根据对应测试用例中需要的组件进行安装

### 根据用例安装其它组件

#### Redis

````shell
docker pull redis:latest
docker run -itd --name redis-test -p 6379:6379 redis
````
