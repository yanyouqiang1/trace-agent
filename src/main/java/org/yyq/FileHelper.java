package org.yyq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
    public static int LEVEL = 0;

    public static FileWriter fileWriter = null;

    public static String FILEPATH = "trace.txt";

    public static void append(String content) {
        FileWriter fw = getFileWriter();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LEVEL; i++) {
            sb.append("\t");
        }
        sb.append(content);
//
//        sb.append("(").append(fileName).append(":").append(lineNumber).append(")");
        try {
            fw.append(sb.toString());
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void incLevel() {
        LEVEL++;
        FileWriter fw = getFileWriter();
        try {
            fw.write("\n");
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void decLevel() {
        LEVEL--;
    }

    public static void clear() {
        LEVEL = 0;
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileWriter = null;
    }


    private static FileWriter getFileWriter() {
        if (fileWriter == null) {
            File file = getFile();
            try {
                fileWriter = new FileWriter(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return fileWriter;
    }


    /**
     * 返回文件，文件存在则清空
     *
     * @return
     */
    private static File getFile() {
        File file = new File(FILEPATH);
        try {
            if (file.exists()) {
                // 清空文件内容
                FileWriter writer = new FileWriter(file, false);
                writer.write("");
                writer.close();
                System.out.println("文件已清空");
            } else {
                // 创建新文件
                if (file.createNewFile()) {
                    System.out.println("文件已创建");
                } else {
                    System.out.println("无法创建文件");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("trace文件保存在：" + file.getPath());
        return file;
    }
}
