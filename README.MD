# 一、编译

mvn assembly:assembly

target包下的[trace-agent-1.0-SNAPSHOT-jar-with-dependencies.jar](target%2Ftrace-agent-1.0-SNAPSHOT-jar-with-dependencies.jar)

也可直接下载[trace-agent.jar](lib%2Ftrace-agent.jar) lib下面的包

# 二、使用

java -javaagent:trace-agent.jar=com.xx youApplicaiton

com.xx 是你要监控的包名。 可以是多个用逗号分割，如

=com.xx,org.alibaba




