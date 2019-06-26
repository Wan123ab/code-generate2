## Pojo、Dao、DaoImpl、Service、ServiceImpl代码生成工具类

1、这是一个代码自动生成工具类，支持Disconf灵活配置、支持多数据源(跨库)，支持多表导出。导出成功后可以下载到本地，为zip文件，需要自行解压。      
会根据数据库表单独建立目录，同时该路径下Java类的包也都分好了，是按照我们项目目前的
配置分包的。另外已提前导入了包信息和必要的注解(如@Data、@Table、@Service等)。各个Java类上已按照我们
目前的模板添加了注释。可以直接复制到项目中使用。

2、访问地址：http://localhost:8092(端口号可以在application.yml中修改)

3、使用前准备工作：  
  (1)在Disconf配置中心添加自己的配置文件code.properties(在resources下已经有一份模板)   
  (2)在resources下Disconf.properties中修改成自己的配置(修改disconf.version即可)
  
4、Disconf修改url或增减数据源后，后台动态数据源会立刻刷新最新配置，无需重启应用。







"# code-generate2" 
"# code-generate2" 
