package com.zjw.pdf_epub_provider.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.spire.pdf.PdfDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.awt.image.RenderedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author yishnee
 */
public class PDFReader {

    /**
     * 获取格式化后的时间信息
     * @param calendar   时间信息
     * @return
     */
    public static String dateFormat( Calendar calendar ){
        if( null == calendar ) {
            return null;
        }
        String date = null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat( pattern );
        date = format.format( calendar.getTime() );
        return date == null ? "" : date;
    }

    /**打印纲要**/
    public static void getPDFOutline(String file){
        try {
            //打开pdf文件流
            FileInputStream fis = new   FileInputStream(file);
            //加载 pdf 文档,获取PDDocument文档对象
            PDDocument document=PDDocument.load(fis);
            //获取PDDocumentCatalog文档目录对象
            PDDocumentCatalog catalog=document.getDocumentCatalog();
            //获取PDDocumentOutline文档纲要对象
            PDDocumentOutline outline=catalog.getDocumentOutline();
            //获取第一个纲要条目（标题1）
            PDOutlineItem item=outline.getFirstChild();

            String sss=new String();

            if(outline!=null){
                //遍历每一个标题1
                while(item!=null){
                    //打印标题1的文本
                    //System.out.println("Item:"+item.getTitle());

                    sss+=item.getTitle()+'\n';

                    //TODO 获取页码
                    if (item.getDestination() instanceof PDPageDestination)
                    {
                        PDPageDestination pd = (PDPageDestination) item.getDestination();
                        //System.out.println("Destination page: " + (pd.retrievePageNumber() + 1));
                        sss+="" + (pd.retrievePageNumber() + 1)+'\n';
                    }
                    else if (item.getDestination() instanceof PDNamedDestination)
                    {
                        PDPageDestination pd = document.getDocumentCatalog().findNamedDestinationPage((PDNamedDestination) item.getDestination());
                        if (pd != null)
                        {
                            //System.out.println("Destination page: " + (pd.retrievePageNumber() + 1));
                            sss+="" + (pd.retrievePageNumber() + 1)+'\n';
                        }
                    }
                    if (item.getAction() instanceof PDActionGoTo)
                    {
                        PDActionGoTo gta = (PDActionGoTo) item.getAction();
                        if (gta.getDestination() instanceof PDPageDestination)
                        {
                            PDPageDestination pd = (PDPageDestination) gta.getDestination();
                            //System.out.println("Destination page: " + (pd.retrievePageNumber() + 1));
                            sss+="" + (pd.retrievePageNumber() + 1)+'\n';
                        }
                        else if (gta.getDestination() instanceof PDNamedDestination)
                        {
                            PDPageDestination pd = document.getDocumentCatalog().findNamedDestinationPage((PDNamedDestination) gta.getDestination());
                            if (pd != null)
                            {
                                //System.out.println("Destination page: " + (pd.retrievePageNumber() + 1));
                                sss+="" + (pd.retrievePageNumber() + 1)+'\n';
                            }
                        }
                    }





                    //获取标题1下的第一个子标题（标题2）
                    PDOutlineItem child=item.getFirstChild();
                    //遍历每一个标题2
                    while(child!=null){
                        //打印标题2的文本
                        //System.out.println("    Child:"+child.getTitle());
                        sss+=child.getTitle()+'\n';


                        //TODO 获取页码
                        if (child.getDestination() instanceof PDPageDestination)
                        {
                            PDPageDestination pd = (PDPageDestination) child.getDestination();
                            //System.out.println("Destination page: " + (pd.retrievePageNumber() + 1));
                            sss+="" + (pd.retrievePageNumber() + 1)+'\n';
                        }
                        else if (child.getDestination() instanceof PDNamedDestination)
                        {
                            PDPageDestination pd = document.getDocumentCatalog().findNamedDestinationPage((PDNamedDestination) child.getDestination());
                            if (pd != null)
                            {
                                //System.out.println("Destination page: " + (pd.retrievePageNumber() + 1));
                                sss+="" + (pd.retrievePageNumber() + 1)+'\n';
                            }
                        }

                        if (child.getAction() instanceof PDActionGoTo)
                        {
                            PDActionGoTo gta = (PDActionGoTo) child.getAction();
                            if (gta.getDestination() instanceof PDPageDestination)
                            {
                                PDPageDestination pd = (PDPageDestination) gta.getDestination();
                                //System.out.println("Destination page: " + (pd.retrievePageNumber() + 1));
                                sss+="" + (pd.retrievePageNumber() + 1)+'\n';
                            }
                            else if (gta.getDestination() instanceof PDNamedDestination)
                            {
                                PDPageDestination pd = document.getDocumentCatalog().findNamedDestinationPage((PDNamedDestination) gta.getDestination());
                                if (pd != null)
                                {
                                    //System.out.println("Destination page: " + (pd.retrievePageNumber() + 1));
                                    sss+="" + (pd.retrievePageNumber() + 1)+'\n';
                                }
                            }
                        }




                        //指向下一个标题2
                        child=child.getNextSibling();
                    }
                    //指向下一个标题1
                    item=item.getNextSibling();
                }
            }
            //System.out.println("1123"+sss);

            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("catalog.txt"));
                out.write(sss);
                out.close();
                System.out.println("catalog.txt创建成功！");
            } catch (IOException e) {
            }

            //关闭输入流
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     * 获取一级目录
     * 暂时无用
     * @param file PDF路径
     */
    public static void getPDFCatalog(String file){
        try {
            //打开pdf文件流
            FileInputStream fis = new   FileInputStream(file);
            //加载 pdf 文档,获取PDDocument文档对象
            PDDocument document=PDDocument.load(fis);
            //获取PDDocumentCatalog文档目录对象
            PDDocumentCatalog catalog=document.getDocumentCatalog();
            //获取PDDocumentOutline文档纲要对象
            PDDocumentOutline outline=catalog.getDocumentOutline();
            //获取第一个纲要条目（标题1）
            if(outline!=null){
                PDOutlineItem item=outline.getFirstChild();
                //遍历每一个标题1
                while(item!=null){
                    //打印标题1的文本
                    System.out.println("Item:"+item.getTitle());
                    //指向下一个标题1
                    item=item.getNextSibling();
                }
            }
            //关闭输入流
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }


    /**
     * 获取文件内容加上字体大小
     *
     * @param file PDF路径
     * @param targetFilePath 生成style.txt路径, 目前使用项目路径
     *                       TODO 路径修改成static
     */
    public static void getFontName(String file,String targetFilePath){
        try {
            //打开pdf文件流
            FileInputStream fis = new   FileInputStream(file);
            //加载 pdf 文档,获取PDDocument文档对象
            PDDocument document=PDDocument.load(fis);
            //TODO 文本信息 + 字体大小
            //PDFTextStripper stripper=new PDFTextStripper();
            PDFTextStripper stripper = new PDFTextStripper() {
                String prevBaseFont = "";
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException
                {
                    StringBuilder builder = new StringBuilder();

                    for (TextPosition position : textPositions)
                    {
                        String baseFont = String.valueOf(position.getFontSizeInPt());
                        if (baseFont != null && !baseFont.equals(prevBaseFont))
                        {
                            builder.append("</p><p style=\"font-size:").append(baseFont).append("pt\">");
                            prevBaseFont = baseFont;
                        }
                        builder.append(position.getUnicode());
                    }

                    writeString(builder.toString());
                }
            };
            String text = stripper.getText(document);

            //System.out.println(text);
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("style.html"));
                out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\n" +
                        "  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"\" xml:lang=\"\">\n" +
                        "<head>\n" +
                        "    <title></title>\n" +
                        "</head>\n" +
                        "  <body><p>");
                out.write(text);
                out.write("</body>");
                out.close();
                System.out.println("style.txt创建成功！");
            } catch (IOException e) {
            }

            //TODO
           /* for (int i = 0; i < document.getNumberOfPages(); ++i)
            {
                PDPage page = document.getPage(i);
                PDResources res = page.getResources();

                for (COSName fontName : res.getFontNames())
                {
                    PDFont font = res.getFont(fontName);
                    System.out.println(font.getWidth(1));
                    System.out.println(font.getName());

                }
            }*/
            //关闭输入流
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     * 获取标题作为目录 目前无用 在PdfUtil中已重写代码
     * @param bookmark
     */
    public static void showBookmark ( HashMap<String, Object> bookmark) {
        System.out.println (bookmark.get ( "Title" )) ;
        @SuppressWarnings("unchecked")
        ArrayList<HashMap<String, Object>> kids =  (ArrayList<HashMap<String, Object>>) bookmark.get ( "Kids" ) ;
        if ( kids == null ) {
            return ;
        }
        for ( Iterator<HashMap<String, Object>> i = kids.iterator () ; i.hasNext () ; ) {
            showBookmark ( i.next ()) ;
        }
    }

    /**
     * 获取标题作为目录 目前无用 在PdfUtil中已重写代码
     * @param bookmark
     */
    public static void getPageNumbers(HashMap<String, Object> bookmark) {
        if (bookmark == null) {
            return;
        }

        if ("GoTo".equals(bookmark.get("Action"))) {
            String page = (String)bookmark.get("Page");
            if (page != null) {
                page = page.trim();
                int idx = page.indexOf(' ');
                int pageNum;
                if (idx < 0){
                    pageNum = Integer.parseInt(page);
                    System.out.println ("pageNum :"+ pageNum) ;
                }
                else{
                    pageNum = Integer.parseInt(page.substring(0, idx));
                    System.out.println ("pageNum:" +pageNum) ;
                }
            }
            @SuppressWarnings("unchecked")
            ArrayList<HashMap<String, Object>> kids =  (ArrayList<HashMap<String, Object>>) bookmark.get ( "Kids" ) ;
            if ( kids == null ) {
                return ;
            }
            for ( Iterator<HashMap<String, Object>> i = kids.iterator () ; i.hasNext () ; ) {
                getPageNumbers ( i.next ()) ;
            }

        }
    }


    /**
     * 获取PDF文档元数据
     * 即是pdf文档中属性信息
     * @param file
     */
    public static void getPDFInformation(String file){
        try {
            //打开pdf文件流
            FileInputStream fis = new FileInputStream(file);
            //加载 pdf 文档,获取PDDocument文档对象
            PDDocument document=PDDocument.load(fis);
            /** 文档属性信息 **/
            PDDocumentInformation info = document.getDocumentInformation();

            System.out.println("页数:"+document.getNumberOfPages());

            System.out.println( "标题:" + info.getTitle() );
            System.out.println( "主题:" + info.getSubject() );
            System.out.println( "作者:" + info.getAuthor() );
            System.out.println( "关键字:" + info.getKeywords() );

            System.out.println( "应用程序:" + info.getCreator() );
            System.out.println( "pdf 制作程序:" + info.getProducer() );

            System.out.println( "Trapped:" + info.getTrapped() );

            System.out.println( "创建时间:" + dateFormat( info.getCreationDate() ));
            System.out.println( "修改时间:" + dateFormat( info.getModificationDate()));

            //关闭输入流
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     * 提取pdf文本
     * 目前无用
     * @param file
     */
    public static void extractTXT(String file){
        try{
            //打开pdf文件流
            FileInputStream fis = new   FileInputStream(file);
            //实例化一个PDF解析器
            PDFParser parser = new PDFParser((RandomAccessRead) fis);
            //解析pdf文档
            parser.parse();
            //获取PDDocument文档对象
            PDDocument document=parser.getPDDocument();
            //获取一个PDFTextStripper文本剥离对象
            PDFTextStripper stripper = new PDFTextStripper();
            //获取文本内容
            String content = stripper.getText(document);
            //打印内容
            System.out.println( "内容:" + content );
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     * 提取部分页面文本
     * 目前无用
     * @param file pdf文档路径
     * @param startPage 开始页数
     * @param endPage 结束页数
     */
    public static void extractTXT(String file,int startPage,int endPage){
        try{
            //打开pdf文件流
            FileInputStream fis = new   FileInputStream(file);
            //实例化一个PDF解析器
            PDFParser parser = new PDFParser((RandomAccessRead) fis);
            //解析pdf文档
            parser.parse();
            //获取PDDocument文档对象
            PDDocument document=parser.getPDDocument();
            //获取一个PDFTextStripper文本剥离对象
            PDFTextStripper stripper = new PDFTextStripper();
            // 设置起始页
            stripper.setStartPage(startPage);
            // 设置结束页
            stripper.setEndPage(endPage);
            //获取文本内容
            String content = stripper.getText(document);
            //打印内容
            System.out.println( "内容:" + content );
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }


    /**
     * 保存图片
     * 目前无用
     * @param file 原始文件路径
     * @param imgSavePath 保存的路径
     * @throws IOException
     */
    public void saveimages(String file,String imgSavePath) throws IOException {
        FileInputStream fis = new   FileInputStream(file);
        //加载 pdf 文档,获取PDDocument文档对象
        PDDocument document=PDDocument.load(fis);
        List<RenderedImage> images = new ArrayList<>();
        images=getImagesFromPDF(document);
        for(RenderedImage image:images){
            //TODO 需要编码
        }

    }

    //获取image的中间方法 如上
    public List<RenderedImage> getImagesFromPDF(PDDocument document) throws IOException {
        List<RenderedImage> images = new ArrayList<>();
        for (PDPage page : document.getPages()) {
            images.addAll(getImagesFromResources(page.getResources()));
        }

        return images;
    }
    //获取image的中间方法 如上
    private List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
        List<RenderedImage> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            } else if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
            }
        }

        return images;
    }

    /**
     * 提取图片并保存
     * 目前无用
     * @param file PDF文档路径
     * @param imgSavePath 图片保存路径
     */
    public static void extractImage(String file,String imgSavePath){
        try{
            //打开pdf文件流
            FileInputStream fis = new   FileInputStream(file);
            //加载 pdf 文档,获取PDDocument文档对象
            PDDocument document=PDDocument.load(fis);


            //* 文档页面信息 *
            //获取PDDocumentCatalog文档目录对象
            /*PDDocumentCatalog catalog = document.getDocumentCatalog();
            //获取文档页面PDPage列表
            List pages = (List) catalog.getPages();
            int count = 1;
            int pageNum=pages.size();   //文档页数
            //遍历每一页
            for( int i = 0; i < pageNum; i++ ){
                //取得第i页
                PDPage page = ( PDPage ) pages.get( i );
                if( null != page ){
                    PDResources resource = page.getResources();
                    //获取页面图片信息
                    Map<String,PDXObjectImage> imgs = resource.getImages();
                    for(Map.Entry<String,PDXObjectImage> me: imgs.entrySet()){
                        //System.out.println(me.getKey());
                        PDXObjectImage img = me.getValue();
                        //保存图片，会自动添加图片后缀类型
                        img.write2file( imgSavePath + count );
                        count++;
                    }
                }
            }*/
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDFReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 提取文本并保存
     * 目前无用
     * @param file PDF文档路径
     * @param savePath 文本保存路径
     */
    public static void extractTXT(String file,String savePath){
        try{
            //打开pdf文件流
            FileInputStream fis = new   FileInputStream(file);
            //实例化一个PDF解析器
            PDFParser parser = new PDFParser((RandomAccessRead) fis);
            //解析pdf文档
            parser.parse();
            //获取PDDocument文档对象
            PDDocument document=parser.getPDDocument();
            //获取一个PDFTextStripper文本剥离对象
            PDFTextStripper stripper = new PDFTextStripper();
            //创建一个输出流
            Writer writer=new OutputStreamWriter(new FileOutputStream(savePath));
            //保存文本内容
            stripper.writeText(document, writer);
            //关闭输出流
            writer.close();
            //关闭输入流
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDFReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 提取部分页面文本并保存
     * 目前无用
     * @param file PDF文档路径
     * @param startPage 开始页数
     * @param endPage 结束页数
     * @param savePath 文本保存路径
     */
    public static void extractTXT(String file,int startPage,
                                  int endPage,String savePath){
        try{
            //打开pdf文件流
            FileInputStream fis = new   FileInputStream(file);
            //实例化一个PDF解析器
            PDFParser parser = new PDFParser((RandomAccessRead) fis);
            //解析pdf文档
            parser.parse();
            //获取PDDocument文档对象
            PDDocument document=parser.getPDDocument();
            //获取一个PDFTextStripper文本剥离对象
            PDFTextStripper stripper = new PDFTextStripper();
            //创建一个输出流
            Writer writer=new OutputStreamWriter(new FileOutputStream(savePath));
            // 设置起始页
            stripper.setStartPage(startPage);
            // 设置结束页
            stripper.setEndPage(endPage);
            //保存文本内容
            stripper.writeText(document, writer);
            //关闭输出流
            writer.close();
            //关闭输入流
            document.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDFReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
