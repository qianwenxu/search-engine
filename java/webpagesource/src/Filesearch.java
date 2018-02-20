import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

public class Filesearch {  
 
  private File INDEXDIR = new File("indexDir");  
 
  private Analyzer ikAnalyzer = new IKAnalyzer();  
 
  public void searchbycontent(String[] fields, String keyword,int startRow , int howManyRow) {  
    Directory directory = null;  
    IndexSearcher searcher = null;  
    IndexReader reader = null;  
    try {  
      //使用 IKQueryParser类提供的parseMultiField方法构建多字段多条件查询  
      Query query = IKQueryParser.parseMultiField(fields, keyword);  
      //如果是使用IKAnalyzer单字段搜索  
      //Query query = IKQueryParser.parse(fieldName, keyword);  
      //如果是用普通MultiFieldQueryParser得到query对象  
      //Query query =MultiFieldQueryParser.parse(Version.LUCENE_CURRENT, keys, fields, flags,analyzer);        
      System.out.println("查询条件:" + query.toString() + " :-< " + QueryParser.escape(keyword));  
      directory = SimpleFSDirectory.open(INDEXDIR);  
      reader = IndexReader.open(new SimpleFSDirectory(INDEXDIR));  
      searcher = new IndexSearcher(reader);  
      searcher.setSimilarity(new IKSimilarity());//在索引器中使用IKSimilarity相似度评估器   
 
      TopScoreDocCollector results = TopScoreDocCollector.create(searcher.maxDoc(), false);  
      searcher.search(query, results);  
      //分页取出指定的doc(开始条数, 取几条)  
      ScoreDoc[] docs = results.topDocs(startRow,howManyRow).scoreDocs;  
 
      //关键字高亮显示的html标签，需要导入lucene-highlighter-3.5.0.jar  
      //SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");  
      //Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
      for(int i = 0; i < docs.length; i++) {  
        Document doc = searcher.doc(docs[i].doc);  
        //标题增加高亮显示     
        //TokenStream tokenStream1 = ikAnalyzer.tokenStream("name", new StringReader(doc.get("name")));  
        //String title = highlighter.getBestFragment(tokenStream1, doc.get("name"));
        String title=doc.get("name");
        String content=doc.get("content");
        //内容增加高亮显示     
        //TokenStream tokenStream2 = ikAnalyzer.tokenStream("content", new StringReader(doc.get("content")));  
        //String content = highlighter.getBestFragment(tokenStream2, doc.get("content"));  
        System.out.println(doc.get("id") + " : " + title + " : " + content+"  pr:"+doc.get("pagerank"));  
      }  
 
    } catch(Exception e) {  
      e.printStackTrace();  
    } finally {  
      if(searcher != null) {  
        try {  
          searcher.close();  
        } catch(IOException e) {  
          e.printStackTrace();  
        }  
      }  
      if(directory != null) {  
        try {  
          directory.close();  
        } catch(IOException e) {  
          e.printStackTrace();  
        }  
      }  
    }  
  }  
 public void searchbytype(String type){
	Directory dir = null;
	IndexReader reader = null;
	IndexSearcher searcher = null;
	try {
		 dir = FSDirectory.open(INDEXDIR);
		 reader = IndexReader.open(dir);
		 searcher = new IndexSearcher(reader);
		 Query query = new TermQuery(new Term("type", type));
		 TopDocs tops = searcher.search(query, 10);
		 ScoreDoc[] docs = tops.scoreDocs;
		 for (ScoreDoc sd : docs) {
			 Document doc = searcher.doc(sd.doc);
			 System.out.print("filename:" + doc.get("name")+"\t");
			 System.out.println();
		 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 } finally {
		 try {
			 reader.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	}
 }
 
 public void searchbysite(String site){
		Directory dir = null;
		IndexReader reader = null;
		IndexSearcher searcher = null;
		try {
			 dir = FSDirectory.open(INDEXDIR);
			 reader = IndexReader.open(dir);
			 searcher = new IndexSearcher(reader);
			 Query query = new TermQuery(new Term("id", site));
			 TopDocs tops = searcher.search(query, 10);
			 ScoreDoc[] docs = tops.scoreDocs;
			 for (ScoreDoc sd : docs) {
				 Document doc = searcher.doc(sd.doc);
				 System.out.print("filename:" + doc.get("name")+"\t");
				 System.out.println();
			 }
			 } catch (Exception e) {
				 e.printStackTrace();
			 } finally {
			 try {
				 reader.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		}
	 }
 public void search(String keyword){
	 if(keyword.startsWith("filetype:")){
		 System.out.println(keyword.substring(9));
		 searchbytype(keyword.substring(9));
	 }
	 else if(keyword.startsWith("site:")){
		 System.out.println(keyword.substring(5));
		 searchbysite(keyword.substring(5));
	 }
	 else{
		 searchbycontent(new String[]{"anchor","name", "content"}, keyword,0,10);  
	 }
 }
  public static void main(String[] args) {  
    Filesearch search = new Filesearch();  
    Scanner in = new Scanner(System.in);
	String str = in.nextLine().toLowerCase();
    search.search(str);
  }  
}
