package utils;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;

public class Utils {
	
	/*
	 * @Parameters: a List of WebElements
	 * Filters WebElemnts not related to the rankings
	 * @Returns: a filtered list of only the first 5 rankings
	*/
	public static List<WebElement> getFilteredRanking(List<WebElement> list) {
		List<WebElement> filteredList = new ArrayList<WebElement>();
		for (int i = 0; i < 10; i+=2) {
			filteredList.add(list.get(i));
		}
		return filteredList;
	}
}
