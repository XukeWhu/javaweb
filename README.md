本项目是基于Spring开发的讨论社区，使用IDEA进行开发，Maven进行管理。
项目中使用了Spring Boot，SSM，Redis，Kafka，Elasticsearch，Caffeine以及Spring Security。
项目中的src存放项目源码，其中包括存放主文件的main文件夹以及存放测试文件的test文件夹，main中各个文件夹的功能如下：
actuator：自定义Endpoint，用于监控应用服务的运行状况。
annotation：自定义注解；
aspect：利用AOP来记录业务层的日志。
config：进行各种配置，包括解释器，ES，Redis等的配置。
controller：控制层，基于业务层实现社区功能，包括注册，登录，发帖，评论，点赞，关注，搜索等。此外，还利用Advice实现了控制层异常的同一处理，利用解释器实现了访问量的统计以及权限管理。
dao：基于Mybatis实现对数据库的访问。
entity：定义各种实体类。
event：定义Kafka的消费者和生产者，实现发送系统消息以及管理ES中的帖子信息。
quartz：定义定时任务，根据点赞量，关注量等信息修改帖子分数。
service：业务层，基于数据访问层dao实现业务功能，包括具体的登录，注册，点赞等功能。
util：实现辅助功能，包括发送邮件，生成UUID，实现敏感词过滤。
项目中的resources文件夹中存放各种项目所需的资源：
mapper：存放Mybatis的映射文件。
static：存放各种项目所需的静态资源，包括图片，js，css，html。
templates：存放各种项目所需的thymeleaf模板文件。



