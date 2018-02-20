package com.pr.xu;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@SuppressWarnings("null")
	@RequestMapping("search")
	public String search(String searchstr,Model model){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("I:\\html\\log.txt",true);
			PrintWriter out0=new PrintWriter(fos);
			Date date = new Date();
		    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String time = format.format(date);
			out0.write(time+":"+searchstr+"\r\n");
			out0.flush();
			out0.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 相对路径，如果没有则要建立一个新的output。txt文件 
		System.out.println("search the word:"+searchstr);
		Filesearch search = new Filesearch();  
		System.out.println("begin search");
		ArrayList<scoredoc> scd=search.search(searchstr);
		model.addAttribute("scd", scd);
		System.out.println("end search");
		for(int i=0;i<scd.size();i++){
			System.out.println(scd.get(i).title);
		}
		return "answer";
	}
	
}
