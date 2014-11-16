import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class PDFToSWF {
	
	
	/**
     * SWTOOLS的安装路径,我的SWFTools安装目录为:"F:\SWFTools"
     */
    public static final String SWFTOOLS_PATH = "F:\\SWFTools";
    /**
     * pdf文件后缀名
     */
    public static final String FILE_NAME_OF_PDF = "pdf";
    /**
     * swf文件后缀名
     */
    public static final String FILE_NAME_OF_SWF = "swf";

    private String inputFP;
    
    public PDFToSWF(String inputFP){
    	this.inputFP = inputFP;
    }
    /**
     * 获得文件的路径
     * 
     * @param file
     *            文件的路径 ,如："c:/test/test.swf"
     * @return 文件的路径
     */
    /*public String getFilePath(String file) {
        String result = file.substring(0, file.lastIndexOf("\\"));
        if (file.substring(2, 3) == "/") {
            result = file.substring(0, file.lastIndexOf("/"));
        } else if (file.substring(2, 3) == "\\") {
            result = file.substring(0, file.lastIndexOf("\\"));
        }
        return result;
    }*/
    
    public String getDestPath(){
    	int i = inputFP.lastIndexOf('.');
        String destPath = inputFP.substring(0, i + 1) + FILE_NAME_OF_SWF;
        return destPath;
    }
    /**
     * 新建一个目录
     * 
     * @param folderPath
     *            新建目录的路径 如："c:\\newFolder"
     */
    /*public void newFolder(String folderPath) {
        try {
            File myFolderPath = new File(folderPath.toString());
            if (!myFolderPath.exists()) {
                myFolderPath.mkdir();
            }
        } catch (Exception e) {
            System.out.println("新建目录操作出错");
            e.printStackTrace();
        }
    }*/

    /**
     * the exit value of the subprocess represented by this Process object. By
     * convention, the value 0 indicates normal termination.
     * 
     * @param sourcePath
     *            pdf文件路径 ，如："c:/hello.pdf"
     * @param destPath
     *            swf文件路径,如："c:/test/test.swf"
     * @return 正常情况下返回：0，失败情况返回：1
     * @throws IOException
     */
    public int convertPDF2SWF(String sourcePath) throws IOException {
    	// 如果目标文件的路径是新的，则新建路径
        String destPath = getDestPath();
        // 源文件不存在则返回
        File source = new File(sourcePath);
        if (!source.exists()) {
            return 0;
        }
        // 调用pdf2swf命令进行转换
        String command = SWFTOOLS_PATH + "\\pdf2swf.exe  -t \"" + sourcePath + "\" -o  \"" + destPath + "\" -s flashversion=9 -s languagedir=D:\\xpdf\\xpdf-chinese-simplified ";        
        //System.out.println("命令操作:" + command + "\n开始转换...");
        // 调用外部程序
        Process process = Runtime.getRuntime().exec(command);
        final InputStream is1 = process.getInputStream();
        new Thread(new Runnable() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                try {
                    while (br.readLine() != null)
                        ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区
        InputStream is2 = process.getErrorStream();
        BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
        // 保存输出结果流
        StringBuilder buf = new StringBuilder();
        String line = null;
        while ((line = br2.readLine()) != null)
            // 循环等待ffmpeg进程结束
            buf.append(line);
        while (br2.readLine() != null)
            ;
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("转换结束...");
        return process.exitValue();
    }

    /**
     * pdf文件转换为swf文件操作
     * 
     * @param sourcePath
     *            pdf文件路径 ，如："c:/hello.pdf"
     * @param destPath
     *            swf文件路径,如："c:/test/test.swf"
     */
    public void pdf2swf() {
        long begin_time = new Date().getTime();
        try {
            convertPDF2SWF(inputFP);
            long end_time = new Date().getTime();
            System.out.println("转换共耗时 :[" + (end_time - begin_time) + "]ms");
            System.out.println("转换文件成功！！");
        } catch (Exception ex) {
        	//ex.printStackTrace();
            System.out.println("转换过程失败！！");
        }
    } 
}
