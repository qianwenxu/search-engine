import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class createindex {  
 
  private File INDEXDIR = new File("indexDir");  
 
  private static Version VERSION = Version.LUCENE_35;  
 
  Analyzer ikAnalyzer = new IKAnalyzer();  
 
  Directory indexDir = null;  
  
  //int n=870;
 
  public void createIndex() {  
    try {  
      IndexWriterConfig config = new IndexWriterConfig(VERSION, ikAnalyzer);  
      config.setOpenMode(OpenMode.CREATE);  
      IndexWriter indexWriter = null;  
      //使用IK中文分词器建立索引  
      indexWriter = new IndexWriter(FSDirectory.open(INDEXDIR), config);   
      BufferedReader urlbr = new BufferedReader(new FileReader("phildoc\\url"));
      BufferedReader prbr = new BufferedReader(new FileReader("pagerank"));
      urlbr = new BufferedReader(new FileReader("wxdoc\\url"));
      for(int i=0;i<50;i++){
    	  Document temp = new Document();  
    	  String urls=urlbr.readLine();
    	  temp.add(new Field("num", String.valueOf(i), Store.YES, Index.ANALYZED));
          temp.add(new Field("id", urls, Store.YES, Index.ANALYZED));
          String prs=prbr.readLine();
          temp.add(new Field("pagerank",prs, Store.YES, Index.ANALYZED));
          BufferedReader br = new BufferedReader(new FileReader("wxdoc\\"+i));
          BufferedReader ancbr = new BufferedReader(new FileReader("wxdoc\\anchor\\"+i));
          String s = br.readLine();
          String ancs=ancbr.readLine();
          s=br.readLine();
          //System.out.print(i+"2s:"+s+"   ");
          temp.add(new Field("content", s, Store.YES, Index.ANALYZED));  
          temp.add(new Field("anchor", ancs, Store.YES, Index.ANALYZED));  
          String type="html";
          if(urls.endsWith("doc")){
        	  type="doc";
          }else if(urls.endsWith("docx")){
        	  type="docx";
          }else if(urls.endsWith("pdf")){
        	  type="pdf";
          }else if(urls.endsWith("rar")){
        	  type="rar";
          }else if(urls.endsWith("xlsx")){
        	  type="xlsx";
          }else if(urls.endsWith("xls")){
        	  type="xls";
          }
          temp.add(new Field("type",type,Store.YES,Index.ANALYZED));
          s=br.readLine();
          if(s==null){
        	  s="";
          }
          //System.out.println(i+"3s:"+s);
          temp.add(new Field("name", s , Store.YES, Index.ANALYZED));  
          indexWriter.addDocument(temp); 
      }
      urlbr.close();
      prbr.close();
      indexWriter.close();  
 
      System.out.println("索引建立完毕");  
    } catch(Exception e) {  
      e.printStackTrace();  
    }  
  }  
 
  public static void main(String[] args) {  
    createindex lucene = new createindex();  
    lucene.createIndex();  
  }  
}
