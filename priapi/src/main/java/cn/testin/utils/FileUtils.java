package cn.testin.utils;

import cn.testin.entry.result.ResultError;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author gaojindan
 * @date 2018/8/10 10:10
 * @des
 */
public class FileUtils {
    /*
     * @author: gaojindan
     * @date: 2018/7/19 下午2:15
     * @des: 生成md文件
     * @param: content:要生成的字段串
     * @param: path:生成md后保存的路径
     * @return:
     */
    public static void createFile(String content,String path) throws IOException {
        byte[] sourceByte = content.getBytes();

        FileWriter fw = null;
        File file = new File(path);		//文件路径（路径+文件名）
        if (!file.exists()) {	//文件不存在则创建文件，先创建目录
            File dir = new File(file.getParent());
            dir.mkdirs();
            file.createNewFile();
        }
        fw = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();

        fw.flush();
        pw.close();
        fw.close();
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

    /**
     *
     * @author Gaojindan
     * @date 2018/8/17 10:58
     * @des 保存文件到指定路径
     * @param file 文件
     * @param path 路径
     * @param fileName 文件名
     * @return
     */
    public static void saveFileToPath(MultipartFile file,String path,String fileName) throws IOException{
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();  //判断并创建文件夹
            }
            byte[] bytes = file.getBytes();
            File fileToSave = new File(dir.getAbsolutePath()  + File.separator +  fileName);
            FileCopyUtils.copy(bytes, fileToSave);   //保存文件

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @author Gaojindan
     * @date 2018/8/17 10:59
     * @des 获取文件扩展名
     * @param
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
}
