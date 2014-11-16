import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.regex.Pattern;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
//import org.artofsolving.jodconverter.OfficeDocumentConverter;
//import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
//import org.artofsolving.jodconverter.office.OfficeManager;
/**
 * 这是一个工具类，主要是为了使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx)
 * 转化为pdf文件<br>
 * Office2010的没测试<br>
 * 
 * @date 2012-11-5
 * @author xhw
 * 
 */
public class OfficeToPDF {

    /**
     * office中.doc格式
     */
    public static final String OFFICE_DOC = "doc";
    /**
     * office中.docx格式
     */
    public static final String OFFICE_DOCX = "docx";
    /**
     * office中.xls格式
     */
    public static final String OFFICE_XLS = "xls";
    /**
     * office中.xlsx格式
     */
    public static final String OFFICE_XLSX = "xlsx";
    /**
     * office中.ppt格式
     */
    public static final String OFFICE_PPT = "ppt";
    /**
     * office中.pptx格式
     */
    public static final String OFFICE_PPTX = "pptx";
    /**
     * pdf格式
     */
    public static final String OFFICE_TO_PDF = "pdf";
    
    private String inputFP;
    private long   time;

    public OfficeToPDF(String inputFP) {
    	this.inputFP = inputFP;
    	this.time    = new Date().getTime();
    }

    //public void init(){
        //OfficeToPDF office2pdf = new OfficeToPDF();
        //openOfficeToPDF("e:\\test." + OFFICE_DOC, "e:\\test_" + new Date().getTime() + "." + OFFICE_TO_PDF);
        //openOfficeToPDF("e:\\test." + OFFICE_PPT, null);
    //}
    /**
     * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件<br>
     * 
     * @param inputFilePath
     *            源文件路径，如："e:\test.docx"
     * @param outputFilePath
     *            目标文件路径，如："e:\test_docx.pdf"
     * @return
     */
    public boolean openOfficeToPDF() {
        return office2pdf(inputFP,null);
    }
    
    public boolean openOfficeToPDF(String outputFilePath) {
        return office2pdf(inputFP, outputFilePath);
    }

    /**
     * 根据操作系统的名称，获取OpenOffice.org 3的安装目录<br>
     * 如我的OpenOffice.org 3安装在：C:/Program Files (x86)/OpenOffice.org 3<br>
     * 
     * @return OpenOffice.org 3的安装目录
     */
    /*public String getOfficeHome() {
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return "/opt/openoffice.org3";
        } else if (Pattern.matches("Windows.*", osName)) {
            return "C:/Program Files (x86)/OpenOffice.org 3";
        } else if (Pattern.matches("Mac.*", osName)) {
            return "/Application/OpenOffice.org.app/Contents";
        }
        return null;
    }*/

    /**
     * 连接OpenOffice.org 并且启动OpenOffice.org
     * 
     * @return
     */
    /*public OfficeManager getOfficeManager() {
        DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
        // 获取OpenOffice.org 3的安装目录
        String officeHome = getOfficeHome();
        config.setOfficeHome(officeHome);
        // 启动OpenOffice的服务
        OfficeManager officeManager = config.buildOfficeManager();
        officeManager.start();
        return officeManager;
    }*/

    /**
     * 转换文件
     * 
     * @param inputFile
     * @param outputFilePath_end
     * @param inputFilePath
     * @param outputFilePath
     * @param converter
     */
    public void converterFile(File inputFile, String outputFilePath_end, String inputFilePath, String outputFilePath, DocumentConverter converter) {
        File outputFile = new File(outputFilePath_end);
        // 假如目标路径不存在,则新建该路径
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        converter.convert(inputFile, outputFile);
        //System.out.println("文件：" + inputFilePath + "\n转换为\n目标文件：" + outputFile + "\n成功！");
    }

    /**
     * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件<br>
     * 
     * @param inputFilePath
     *            源文件路径，如："e:/test.docx"
     * @param outputFilePath
     *            目标文件路径，如："e:/test_docx.pdf"
     * @return
     */
    public boolean office2pdf(String inputFilePath, String outputFilePath) {
        boolean flag = false;
        //OfficeManager officeManager = getOfficeManager();
        try{
        String OpenOffice_HOME = "C:\\Program Files\\OpenOffice 4\\";
        String command = OpenOffice_HOME  
                + "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";  
        Process pro = Runtime.getRuntime().exec(command);
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(  
                "127.0.0.1", 8100);  
        connection.connect();  
        // 连接OpenOffice
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        //long begin_time = new Date().getTime();
        if (null != inputFilePath) {
            File inputFile = new File(inputFilePath);
            // 判断目标文件路径是否为空
            if (null == outputFilePath) {
                // 转换后的文件路径
                String outputFilePath_end = getOutputFilePath();
                if (inputFile.exists()) {// 找不到源文件, 则返回
                    converterFile(inputFile, outputFilePath_end, inputFilePath, outputFilePath, converter);
                    flag = true;
                }
            } else {
                if (inputFile.exists()) {// 找不到源文件, 则返回
                    converterFile(inputFile, outputFilePath, inputFilePath, outputFilePath, converter);
                    flag = true;
                }
            }
            //officeManager.stop();
            connection.disconnect();  
            pro.destroy();
        } else {
            System.out.println("con't find the resource");
        }
        }catch (Exception e) {  
            e.printStackTrace();   
        }
        //long end_time = new Date().getTime();
        //System.out.println("文件转换耗时：[" + (end_time - begin_time) + "]ms");
        return flag;
    }

    /**
     * 获取输出文件
     * 
     * @param inputFilePath
     * @return
     */
    public String getOutputFilePath() {
        String outputFilePath = inputFP.replaceAll("." + getPostfix(), ".pdf");
    	//String outputFilePath = "F:\\MyEclipse 10\\PKM\\WebRoot\\js\\FlexPaper\\temp_" + time + "." + OFFICE_TO_PDF;
        return outputFilePath;
    }

    /**
     * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"<br>
     * 
     * @param inputFilePath
     * @return
     */
    public String getPostfix() {
        return inputFP.substring(inputFP.lastIndexOf(".") + 1);
    }

}