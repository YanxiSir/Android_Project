package com.neu.tools;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.neu.javabean.DaysArrangement;
import com.neu.javabean.TeacherBancheInfo;

public class ResultInfoTools {

	public static String getResultInfo(String xmlInfo) {

		try {
			
			Parser parser = new Parser(xmlInfo);
			NodeFilter filter = new TagNameFilter("td");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);
			/*for (int i = 0; i < nodes.size(); i++) {
				System.out.println(nodes.elementAt(i).toPlainTextString()
						.toString());

			}*/
			return nodes.elementAt(0).toPlainTextString().toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		return "";
	}

	public static NodeList getBancheInfoStr(String htmlStr,String className) throws ParserException{
		Parser myParser = new Parser(htmlStr);
		NodeList nodeList = null;
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		NodeFilter attrFilter = new HasAttributeFilter("class",className);
		NodeFilter andFilter = new AndFilter(tableFilter, attrFilter);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] { andFilter});
		nodeList = myParser.parse(lastFilter);
		return nodeList;
		
	}
	public static NodeList getBancheInfo(URL url,String className) throws ParserException, IOException {
			
			Parser myParser = new Parser(url.openConnection());
			NodeList nodeList = null;
			NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
			NodeFilter attrFilter = new HasAttributeFilter("class",className);
			NodeFilter andFilter = new AndFilter(tableFilter, attrFilter);
			OrFilter lastFilter = new OrFilter();
			lastFilter.setPredicates(new NodeFilter[] { andFilter});
			nodeList = myParser.parse(lastFilter);
			return nodeList;
		
	}
	public static ArrayList<TeacherBancheInfo> getTeacherBancheInfo(InputStream is){
		ArrayList<TeacherBancheInfo> infos = null;
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is,"utf-8");
			int eventType = parser.getEventType();
			
			TeacherBancheInfo campusInfo = null;
			ArrayList<DaysArrangement> days = null;
			DaysArrangement dayInfo = null;
			ArrayList<String> times = null;
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if("东北大学".equals(parser.getName()))
						infos = new ArrayList<TeacherBancheInfo>();
					else if("出发地".equals(parser.getName())){
						campusInfo = new TeacherBancheInfo();
					}else if("校区名称".equals(parser.getName())){
						String campusName = parser.nextText();
						campusInfo.setCampusName(campusName);
					}else if("发车地点".equals(parser.getName())){
						String startingPlace = parser.nextText();
						campusInfo.setStartingPlace(startingPlace);
					}else if("发车时间".equals(parser.getName())){
						days = new ArrayList<DaysArrangement>();
					}else if("日期".equals(parser.getName())){
						dayInfo = new DaysArrangement();
						times = new ArrayList<String>();
						String date = parser.getAttributeValue(0);
						dayInfo.setDaysDate(date); 
					}else if("string".equals(parser.getName())){
						String time = parser.nextText();
						times.add(time);
					}
					
					
					
					break;
				case XmlPullParser.END_TAG:
					if("日期".equals(parser.getName())){
						dayInfo.setTimes(times);
						days.add(dayInfo);
					}else if("出发地".equals(parser.getName())){
						campusInfo.setDays(days);
						infos.add(campusInfo);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
			is.close();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) { 
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return infos;
	}


}
