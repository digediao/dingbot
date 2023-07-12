用户可以在azure_openai_gpt35.java中改变自己的azure openai订阅中的密钥、终结点、部署名，以连接不同的openai订阅
虚拟机需要准备：nginx、redis、jar包及运行环境
并发(顺序处理、放入队列)、改变连接方式（原来是HttpURLConnection）、与redis的连接、添加日志

客户需要改的地方：application.yml | config.properties

# 功能模块
rabbitmq、nginx、redis、jre

# 功能

### rabbitmq对消息的处理
