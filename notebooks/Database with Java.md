# Database with Java 

sql , 发音sequel, structured query language

### SQL的好处:

1. declarative  直接说你要啥就行, 不用知道how
2. implement widely 
3. constrained   not target at Turing-complete tasks 图灵完全性通常指“具有无限存储能力的通用物理机器或编程语言”。
4. 很多feature, extensible  



relation 是一个 unordered set .

attribute  = column = field

tuple = record = row

https://sqlzoo.net/   网站指南

DDL - data definition language  -> define and modify schema

```sql
CREATE TABLE Sailors(
    sid INTEGER,
    sname CHAR(20),
    rating INTEGER,
    PRIMARY KEY (sid));
CREATE TABLE Reserves(
    sid INTEGER,
  	bid INTEGER,
    PRIMARY KEY (sid));
```



DML - data manipulation language -> queries can be written intuitively



### relational model

#### foreign key 

外键并不是通过列名实现的，而是通过定义外键约束实现的：

其中，外键约束的名称`fk_class_id`可以任意，`FOREIGN KEY (class_id)`指定了`class_id`作为外键，`REFERENCES classes (id)`指定了这个外键将关联到`classes`表的`id`列（即`classes`表的主键）。

通过定义外键约束，关系数据库可以保证无法插入无效的数据。即如果`classes`表不存在`id=99`的记录，`students`表就无法插入`class_id=99`的记录。

由于外键约束会降低数据库的性能，大部分互联网应用程序为了追求速度，并不设置外键约束，而是仅靠应用程序自身来保证逻辑的正确性。这种情况下，`class_id`仅仅是一个普通的列，只是它起到了外键的作用而已。

要删除一个外键约束，也是通过`ALTER TABLE`实现的：

```
ALTER TABLE students
DROP FOREIGN KEY fk_class_id;
```

注意：删除外键约束并没有删除外键这一列。删除列是通过`DROP COLUMN ...`实现的。

#### 多对多

通过一个表的外键关联到另一个表，我们可以定义出一对多关系。有些时候，还需要定义“多对多”关系。例如，一个老师可以对应多个班级，一个班级也可以对应多个老师，因此，班级表和老师表存在多对多关系。

多对多关系实际上是通过两个一对多关系实现的，即通过一个中间表，关联两个一对多关系，就形成了多对多关系：



Procedural

Non-Procedural



### 命令

```sql
SELECT *FROM Books 查出所有记录
SELECT 语句中, FROM 必不可少, 告诉数据库在哪个表上查询数据
where price <= 29.9 可以加限定 
sql 用 = 表示相等和<>  不等.  而不是== 和!=
where Title NOT LIKE '$n_x%'  排除所有unix和linux的书名
%表示0个或多个,下划线 _ 表示单个.字符串用单括号, 
where Title  LIKE '%''%' 返回所有包含单引号的书名  
DELETE FROM XXX 删除
UPDATE xxx
SET XXX  修改数据
INSERT INTO xxx
VALUES ('XXX','AAA',)// 注意不是双引号""

```





## JDBC

### 简介

ODBC 为C语言 访问数据库 提供了接口.

通过JDBC  API 编写的程序, 可以与驱动管理器进行通信, 而驱动管理器通过驱动程序和数据库通信.

#### JDBC经典用法

传统 上, JDBC驱动程序部署在client , 数据库部署在server.

现在 ,客户端调用服务器上的中间件, 然后由中间件完成数据库查询操作. 

##### 优点

可视化放在客户端, 业务逻辑放在中间层, 可以从不同的客户端(Java应用, applet, web 表单) 来访问相同的数据和相同的业务规则. 

客户端和中间层,用web 表单HTTP 或调用RMI (Java应用, applet)

 JDBC 负责中间层和后台数据库通信. 



java 操作数据库可以用Derby,清华源下载. 

https://www.cnblogs.com/azhqiang/p/3876214.html



关于插入语句的用法: 

http://c.biancheng.net/view/2574.html