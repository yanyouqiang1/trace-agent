# 一、作用
可以线程经过特定路径的类和方法。 保存以 线程名_trace.txt 的文件中。可以方便的常看线程执行路径，方便熟悉项目

# 二、编译

mvn assembly:assembly

target包下的[trace-agent-1.0-SNAPSHOT-jar-with-dependencies.jar](target%2Ftrace-agent-1.0-SNAPSHOT-jar-with-dependencies.jar)

也可直接下载[trace-agent.jar](lib%2Ftrace-agent.jar) lib下面的包

# 三、使用

java -javaagent:trace-agent.jar=com.xx youApplicaiton

com.xx 是你要监控的包名。 可以是多个用逗号分割，如

=com.xx,org.alibaba

# 四、观察
日志会输出文件保存的位置

```text
org.ClientTest.main(String[])(org.ClientTest.java:8)
org.ClientTest$Person.saySomething()(org.ClientTest$Person.java:33)
org.ClientTest$Person.saySomething()(org.ClientTest$Person.java:33)
org.ClientTest$Person.saySomething()(org.ClientTest$Person.java:33)
org.ClientTest$Person.saySomething()(org.ClientTest$Person.java:33)
org.ClientTest$Person.saySomething()(org.ClientTest$Person.java:33)
org.ClientTest$Person.saySomething()(org.ClientTest$Person.java:33)
```




