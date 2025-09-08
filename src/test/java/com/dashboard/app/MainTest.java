package com.dashboard.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {

	public static void main (String args[]) {
		System.out.println(new MainTest().print(true));
		System.out.println(extractPageName("restricted/trader-margins-new.xhtml"));
	}
	public static String extractPageName(String input) {
        if(input == null) {
            return input;
        }
        String regex = "restricted/([a-zA-Z0-9\\-]+)\\.xhtml.*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return convertToTitleCase(matcher.group(1));
        }
        return null;
    }
	private static String convertToTitleCase(String pageName) {
        if (pageName == null || pageName.isEmpty()) {
            return pageName;
        }

        Pattern pattern = Pattern.compile("(^|-)([a-z])");
        Matcher matcher = pattern.matcher(pageName.toLowerCase());

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, " " + matcher.group(2).toUpperCase());
        }
        matcher.appendTail(result);

        return result.toString().trim();
    }
	public String print(boolean istest) {
		String testRtr = "initialize";
		
		try {
			
			if( istest )
				return "try";
		} catch(Exception e) {
			System.out.println("exception");
		}
		finally {
			System.out.println("finally");
		}
		return testRtr ;
	}
}
