import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class WebPageSource {
	private static final String CHARSET = "UTF-8";
	ArrayList<String> allurlSet = new ArrayList<String>();
	static HashMap<String,Integer> urlindex = new HashMap<String,Integer>();
	HashMap<String, Integer> depth = new HashMap<String, Integer>();
	ArrayList<String> notCrawlurlSet = new ArrayList<String>();
	int crawDepth = 4;
	int threadCount = 20;
	int count = 0;
	static int i=0;
	public static final Object signal = new Object();// �̼߳�ͨ��
	public static void main(String args[]) {
		final WebPageSource wps = new WebPageSource();
		wps.addUrl("http://wxy.nankai.edu.cn/", 1);
		long start = System.currentTimeMillis();
		System.out.println("**************��ʼ����**************");
		wps.begin();
	       while(true){  
	            if(wps.notCrawlurlSet.isEmpty()&& Thread.activeCount() == 1||wps.count==wps.threadCount){  
	                long end = System.currentTimeMillis();  
	                System.out.println("�ܹ�����"+i+"����ҳ");  
	                System.out.println("�ܹ���ʱ"+(end-start)/1000+"��");  
	                System.exit(1);  
//	              break;  
	            }
	       }

	}
	
	private void begin() {
		for (int i = 0; i < threadCount; ++i) {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						String tmp = getAUrl();
						if (tmp != null) {
							crawler(tmp);
						} else {
							synchronized (signal) {
								try {
									count++;
									System.out.println(Thread.currentThread().getName() + ": �ȴ�");
									signal.wait();
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}
					}

				}
			}, "thread-" + i).start();
		}
	}
	
    void crawler(String theurl){    
    	try {
            //��ȡ������վ�ĸ��ڵ㣬Ҳ����html��ͷ����һֱ������
    		File writename = new File("wxdoc\\"+urlindex.get(theurl).toString()); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
			writename.createNewFile();// �������ļ�  
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			File writename1 = new File("wxdoc\\html\\"+urlindex.get(theurl).toString()+".html"); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
			writename.createNewFile();// �������ļ�  
			BufferedWriter out1 = new BufferedWriter(new FileWriter(writename1));
            Document document = Jsoup.connect(theurl).timeout(500000).ignoreHttpErrors(true).ignoreContentType(true).followRedirects(true).post();
            out1.write(document.toString());
            out1.flush();
            out1.close();
            int d = depth.get(theurl);
            Elements es = document.select("a");
            Elements a = document.getElementsByTag("p");
            Elements title=document.getElementsByTag("title");
            String temp = "";
            String tmpanchor="";
            for (Element e : es) {
            	temp = e.attr("href");
            	if (temp != null) {
            		if (!temp.startsWith("http")) {
                        if (temp.startsWith("/"))
                          temp = "http://wxy.nankai.edu.cn" + temp;
                        else
                        	temp="";
                    }
        			/*if(temp.endsWith("/"))
                        temp = temp.substring(0, temp.length() - 1);*/
            		synchronized (signal) {  
            			if(allurlSet.contains(temp)&& temp.startsWith("http://wxy.nankai.edu.cn")){
            				String tmpindex=urlindex.get(temp).toString();
            				out.write(" "+tmpindex);
            				tmpanchor=e.text();
            				FileOutputStream tmpanc=new FileOutputStream("wxdoc\\anchor\\"+tmpindex,true); // ���·�������û����Ҫ����һ���µ�output��txt�ļ� 
            				PrintWriter outanc=new PrintWriter(tmpanc);
            				outanc.write(tmpanchor+"   ");
            				outanc.flush();
            				outanc.close();
            			}
            			else if(i>49){}
            			else if (!allurlSet.contains(temp)&& temp.startsWith("http://wxy.nankai.edu.cn")){
            				addUrl(temp, d + 1); 
            				String tmpindex=urlindex.get(temp).toString();
            				out.write(" "+tmpindex);
            				tmpanchor=e.text();
            				FileOutputStream tmpanc=new FileOutputStream("wxdoc\\anchor\\"+tmpindex,true); // ���·�������û����Ҫ����һ���µ�output��txt�ļ� 
            				PrintWriter outanc=new PrintWriter(tmpanc);
            				outanc.write(tmpanchor+"   ");
            				outanc.flush();
            				outanc.close();
                            if (count > 0) {  
                                signal.notify();  
                                count--;  
                            }
            			}
                    }
            	}  
            }
            out.write("\r\n");
            //System.out.println(a.text());
            out.write(a.text()+"\r\n"); // \r\n��Ϊ����  
            out.write(title.text());
	    	out.flush();
	    	out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	public synchronized String getAUrl() {
		if (notCrawlurlSet.isEmpty())
			return null;
		String tmpAUrl;
		tmpAUrl = notCrawlurlSet.get(0);
		notCrawlurlSet.remove(0);
		return tmpAUrl;
	}
	public synchronized void addUrl(String url, int d) {
			notCrawlurlSet.add(url);
			allurlSet.add(url);
			try {
				FileOutputStream fos=new FileOutputStream("wxdoc\\url",true); // ���·�������û����Ҫ����һ���µ�output��txt�ļ� 
				PrintWriter out0=new PrintWriter(fos);
				out0.write(url+"\r\n");
				urlindex.put(url,i++);
				depth.put(url, d);
				out0.flush();
				out0.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}