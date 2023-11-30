package org.yyq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileHelper {
    public static final String FILE_SUFFIX = "trace.txt";

    public static Map<String, FileInfo> fileInfoMap = new ConcurrentHashMap<>();

    public static void append(String content) {
        FileInfo fileInfo = getFileInfo();

        FileWriter fw = fileInfo.fileWriter;

        StringBuilder sb = new StringBuilder();

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
        FileInfo fileInfo = getFileInfo();
        FileWriter fw = fileInfo.fileWriter;
        try {
            fw.write("\n");
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear() {
        fileInfoMap.values().forEach(fileInfo -> {
            try {
                if (fileInfo.fileWriter != null) {
                    fileInfo.fileWriter.close();
                    fileInfo.fileWriter = null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        fileInfoMap.clear();
    }

    /**
     * 获取，没有就初始化
     *
     * @return
     */
    private static FileInfo getFileInfo() {
        String key = Thread.currentThread().getName();
        FileInfo fileInfo = fileInfoMap.get(key);
        if (fileInfo == null) {
            try {
                File file = getFile();
                fileInfo = new FileInfo(new FileWriter(file));
                fileInfoMap.put(key, fileInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return fileInfo;
    }

    /**
     * 返回文件，文件存在则清空
     *
     * @return
     */
    private static File getFile() {
        String filepath = Thread.currentThread().getName() + "_" + FILE_SUFFIX;
        File file = new File(filepath);
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
        System.out.println("trace文件保存在：" + file.getAbsolutePath());
        return file;
    }


    static class FileInfo {
        FileWriter fileWriter;

        public FileInfo(FileWriter fileWriter) {
            this.fileWriter = fileWriter;
        }
    }
}
