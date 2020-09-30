package net.gpai.test;

import net.gpai.util.FileUtil;

import java.io.File;
import java.nio.charset.Charset;

public class ReadByteToString {

    public static void main(String[] args) {

//        File file = new File("C:\\Users\\YX\\IdeaProjects\\msgConvert\\byteFile.txt");
        File file = new File("C:\\Users\\YX\\IdeaProjects\\simpleHttpServer\\byteFile.txt");

        String context = FileUtil.readFile(file, "UTF-8");

        String[] split = context.split(",");

        int length = split.length;

        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            int intValue = Integer.parseInt(split[i]);
            bytes[i] = (byte) intValue;
        }

        String text = new String(bytes, Charset.forName("UTF-8"));

        System.out.println("源文本内容：\r\n" + text);

    }

}
