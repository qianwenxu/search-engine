package com.pr.xu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;

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
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;


public class Filesearch {  
 
  private File INDEXDIR = new File("J:\\java\\WebPageSource\\indexDir");  
 
  private Analyzer ikAnalyzer = new IKAnalyzer();  
 
  public ArrayList<scoredoc> searchbycontent(String[] fields, String keyword,int startRow , int howManyRow) {
    Directory directory = null;  
    IndexSearcher searcher = null;  
    IndexReader reader = null;  
    try {  
      Query query = IKQueryParser.parseMultiField(fields, keyword);         
      System.out.println("��ѯ����:" + query.toString() + " :-< " + QueryParser.escape(keyword));  
      directory = SimpleFSDirectory.open(INDEXDIR);  
      reader = IndexReader.open(new SimpleFSDirectory(INDEXDIR));  
      searcher = new IndexSearcher(reader);  
      searcher.setSimilarity(new IKSimilarity());//����������ʹ��IKSimilarity���ƶ�������   
      TopScoreDocCollector results = TopScoreDocCollector.create(searcher.maxDoc(), false);  
      searcher.search(query, results);  
      ScoreDoc[] docs = results.topDocs(startRow,howManyRow).scoreDocs;    
      SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");  
      Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
      ArrayList<scoredoc> sdoc =new ArrayList<scoredoc>();
      for(int i = 0; i < docs.length; i++) {  
    	  scoredoc sc=new scoredoc();
        Document doc = searcher.doc(docs[i].doc);
        TokenStream tokenStream1 = ikAnalyzer.tokenStream("name", new StringReader(doc.get("name")));  
        String title = highlighter.getBestFragment(tokenStream1, doc.get("name"));
        String content=doc.get("content");
        sc.id=doc.get("id");
        sc.content=content;
        sc.pr=doc.get("pagerank");
        sc.theid=doc.get("num");
        if(title==null){
        	sc.title=doc.get("name");
        }else{
        	sc.title=title;
        }
        sdoc.add(sc);
        //System.out.println(doc.get("id") + " : " + title + " : " + content+"  pr:"+doc.get("pagerank"));  
        File writename = new File("I://html//"+i+".html"); // 相对路径，如果没有则要建立一个新的output。txt文件  
		writename.createNewFile();// 创建新文件  
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));
		out.write("<html><meta charset=\"UTF-8\"><title>"+sc.title+"</title><p><h3>"+sc.title+"</h3><p><p><h5>"+content+"</h5></p></html>");
		out.flush();
		out.close();
      }
      return sdoc;
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
	return null;  
  }  
 
  
  
  @SuppressWarnings("resource")
public ArrayList<scoredoc> searchbytype(String type){
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
			 ArrayList<scoredoc> sdoc =new ArrayList<scoredoc>();
			 SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");  
		      Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
			 for (int i = 0; i < docs.length; i++) {
				 scoredoc sc=new scoredoc();
				 Document doc = searcher.doc(docs[i].doc);
				 //System.out.print("filename:" + doc.get("name")+"\t");
				 //System.out.println();
				 TokenStream tokenStream1 = ikAnalyzer.tokenStream("name", new StringReader(doc.get("name")));  
			     String title = highlighter.getBestFragment(tokenStream1, doc.get("name"));
				 sc.id=doc.get("id");
			        sc.content=doc.get("content");
			        sc.pr=doc.get("pagerank");
			        sc.theid=doc.get("num");
			        if(title==null){
			        	sc.title=doc.get("name");
			        }else{
			        	sc.title=title;
			        }
			        sdoc.add(sc);
			        File writename = new File("I://html//"+i+".html"); // 相对路径，如果没有则要建立一个新的output。txt文件  
					writename.createNewFile();// 创建新文件  
					BufferedWriter out = new BufferedWriter(new FileWriter(writename));
					out.write("<html><meta charset=\"UTF-8\"><title>"+sc.title+"</title><p><h3>"+sc.title+"</h3><p><p><h5>"+ sc.content+"</h5></p></html>");
					out.flush();
					out.close();
			 }
			 return sdoc;
		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			 try {
				 reader.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
		return null; 
	 }
  
  
  
  @SuppressWarnings("resource")
public ArrayList<scoredoc> searchbysite(String site){
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
			 ArrayList<scoredoc> sdoc =new ArrayList<scoredoc>();
			 SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");  
		      Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
		      for (int i = 0; i < docs.length; i++) {
		    	  	 scoredoc sc=new scoredoc();
					 Document doc = searcher.doc(docs[i].doc);
					 TokenStream tokenStream1 = ikAnalyzer.tokenStream("name", new StringReader(doc.get("name")));  
				     String title = highlighter.getBestFragment(tokenStream1, doc.get("name"));
					 sc.id=doc.get("id");
				     sc.content=doc.get("content");
				     sc.pr=doc.get("pagerank");
				     sc.theid=doc.get("num");
				     if(title==null){
				     	sc.title=doc.get("name");
				     }else{
				     	sc.title=title;
				     }
				     sdoc.add(sc);
				     File writename = new File("I://html//"+i+".html"); // 相对路径，如果没有则要建立一个新的output。txt文件  
					 writename.createNewFile();// 创建新文件  
					 BufferedWriter out = new BufferedWriter(new FileWriter(writename));
					 out.write("<html><meta charset=\"UTF-8\"><title>"+sc.title+"</title><p><h3>"+sc.title+"</h3><p><p><h5>"+ sc.content+"</h5></p></html>");
					 out.flush();
					 out.close();
			 }
		      return sdoc;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 try {
				 reader.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		}
		return null; 
	 }
  
public ArrayList<scoredoc> search(String keyword){
		ArrayList<scoredoc> sdoc =new ArrayList<scoredoc>();
		 if(keyword.startsWith("filetype:")){
			 System.out.println(keyword.substring(9));
			 sdoc=searchbytype(keyword.substring(9));
		 }
		 else if(keyword.startsWith("site:")){
			 System.out.println(keyword.substring(5));
			 sdoc=searchbysite(keyword.substring(5));
		 }
		 else{
			 sdoc=searchbycontent(new String[]{"anchor","name", "content"}, keyword,0,10);  
		 }
		 return sdoc;
	 }
  
  /*public static void main(String[] args) {  
    Filesearch search = new Filesearch();  
    Scanner in = new Scanner(System.in);
	String str = in.nextLine().toLowerCase();
    search.search(new String[]{"name", "content"}, str,0,10);  
  } */ 
}
