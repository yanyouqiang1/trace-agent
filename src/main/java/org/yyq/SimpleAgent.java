package org.yyq;

import cn.hutool.core.util.StrUtil;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleAgent {

    static ClassPool classPool = ClassPool.getDefault();

    /**
     * jvm 参数形式启动，运行此方法
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        if (StrUtil.isBlank(agentArgs)) {
            return;
        }

        String[] packages = agentArgs.split(",");

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                // 通过类名获取CtClass对象
                String dotClassName = className.replace('/', '.');

                if (dotClassName.contains("org.yyq")) {
                    //过滤自己的包
                    return classfileBuffer;
                }
                //过滤一些cglib，proxy动态代理的类
                List<String> proxiedClassName = Arrays.asList("$$EnhancerByCGLIB", "$Proxy");
                if (proxiedClassName.stream().anyMatch(s -> dotClassName.contains(s))) {
                    return classfileBuffer;
                }
                //不再增强包路径里面
                if (!Arrays.stream(packages).anyMatch(pack -> dotClassName.contains(pack))) {
                    return classfileBuffer;
                }
                try {
                    CtClass ctClass = classPool.get(dotClassName);
                    // 遍历所有方法
                    for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
                        //空method过滤
                        if (ctMethod.isEmpty()) {
                            continue;
                        }
                        //常见一些get set 没什么意义的方法过滤
                        if (ctMethod.getName().startsWith("get") || ctMethod.getName().startsWith("set")) {
                            continue;
                        }

                        // 在方法前后添加打印日志的代码
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(ctClass.getName()).append(".").append(ctMethod.getName());
                        String params = Arrays.stream(ctMethod.getParameterTypes()).map(ct -> ct.getSimpleName()).collect(Collectors.joining(","));
                        stringBuilder.append("(").append(params).append(")");
                        stringBuilder.append("(").append(ctClass.getName()).append(".java").append(":").append(ctMethod.getMethodInfo().getLineNumber(0)).append(")");
                        ctMethod.insertBefore("org.yyq.FileHelper.append(\"" + stringBuilder.toString() + "\");org.yyq.FileHelper.incLevel();");
                    }
                    // 返回修改后的字节码
                    return ctClass.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * 动态 attach 方式启动，运行此方法
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("agentmain");
    }
}
