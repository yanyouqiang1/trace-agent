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

                if (!Arrays.stream(packages).allMatch(pack -> dotClassName.contains(pack))) {
                    //不再增强包里面
                    return classfileBuffer;
                }
                try {
                    CtClass ctClass = classPool.get(dotClassName);
                    // 遍历所有方法
                    for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
                        if (ctMethod.isEmpty()) {
                            continue;
                        }
                        // 在方法前后添加打印日志的代码
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(ctClass.getName()).append(".").append(ctMethod.getName());
                        String params = Arrays.stream(ctMethod.getParameterTypes()).map(ct -> ct.getSimpleName()).collect(Collectors.joining(","));
                        stringBuilder.append("(").append(params).append(")");
                        stringBuilder.append("(").append(ctClass.getName()).append(".java").append(":").append(ctMethod.getMethodInfo().getLineNumber(0)).append(")");
                        ctMethod.insertBefore("com.my.FileHelper.append(\"" + stringBuilder.toString() + "\");com.my.FileHelper.incLevel();");
                        ctMethod.insertAfter("com.my.FileHelper.decLevel();");
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
