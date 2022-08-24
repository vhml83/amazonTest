package pageObjects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tests.AmazonTest;
import utils.Utils;

public class AmazonPage {
	
	WebDriver driver;
	private static Logger log = LogManager.getLogger(AmazonTest.class.getName());
	// List<WebElement> Locators
	private final String PRODUCT_NAMES_CLASSNAME = "a-size-base-plus";
	private final String PRODUCT_PRICES_CSS = "span.a-price-whole";
	private final String PRODUCT_RANKING_XPATH = "//div[@class='a-section a-spacing-none a-spacing-top-micro'] /div[@class='a-row a-size-small'] /span";
	// Find By Locators
	By searchBox = By.id("twotabsearchtextbox");
	By searchBoxButton = By.id("nav-search-submit-button");
	By checkBox = By.linkText("Procase");
	By priceRange = By.linkText("$350 a $750");
	
	public AmazonPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void setInputText(String productName) {
		driver.findElement(searchBox).sendKeys(productName);
	}
	
	public void clickSearcSubmitButton() {
		driver.findElement(searchBoxButton).submit();
	}
	
	public void clickCheckBox() {
		driver.findElement(checkBox).click();
	}
	
	public void setPriceRange() {
		driver.findElement(priceRange).click();
	}
	
	public List<String> getproductNames() {
		List<WebElement> webElementList = driver.findElements(By.className(PRODUCT_NAMES_CLASSNAME));
		List<WebElement> first5NamesList = new ArrayList<WebElement>(webElementList.subList(0, 5));
		List<String> productNameList = new ArrayList<>();
		for (WebElement webElement : first5NamesList) {
			productNameList.add(webElement.getText());
		}
		return productNameList;
	}
	
	public List<Integer> getproductPrices() {
		List<WebElement> webElementList = driver.findElements(By.cssSelector(PRODUCT_PRICES_CSS));
		List<WebElement> first5PricesList = new ArrayList<WebElement>(webElementList.subList(0, 5));
		List<Integer> productPriceList = new ArrayList<>();
		for (WebElement webElement : first5PricesList) {
			productPriceList.add(Integer.parseInt(webElement.getText()));
		}
		return productPriceList;
	}
	
	public List<Float> getProductRankings() {
		List<WebElement> webElementList = driver.findElements(By.xpath(PRODUCT_RANKING_XPATH));
		List<WebElement> first5RankingsList = Utils.getFilteredRanking(webElementList);
		List<Float> productRankingList = new ArrayList<>();
		for (WebElement webElement : first5RankingsList) {
			productRankingList.add(Float.parseFloat(webElement.getAttribute("aria-label").substring(0,3)));
		}
		return productRankingList;
	}
	
	public void logFirst5ProductNames() {
		List<String> first5ProductNames = this.getproductNames();
		int i = 1;
		for (String name : first5ProductNames) {
			log.info("Product Name #" + i + ": " + name);
			i++;
		}
	}
	
	public void logFirst5ProductPrices() {
		List<Integer> first5ProductPrices = this.getproductPrices();
		int i = 1;
		for (Integer price : first5ProductPrices) {
			log.info("Product Price #" + i + ": $" + price);
			i++;
		}
	}
	
	public void logFirst5ProductRankings() {
		List<Float> first5Rankings = this.getProductRankings();
		int i = 1;
		for (Float ranking : first5Rankings) {
			log.info("Product Ranking #" + i + ": " + ranking);
			i++;
		}
		
	}
	
	public Map<String, Integer> sortByPrice() {
		List<String> productNames = this.getproductNames();
		List<Integer> productPrices = this.getproductPrices();
		Map<String, Integer> unsortedNamesPricesMap = new HashMap<String, Integer>();
		LinkedHashMap<String, Integer> sortedNamesPricesMap = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < productNames.size(); i++) {
			unsortedNamesPricesMap.put(productNames.get(i), productPrices.get(i));
		}
		unsortedNamesPricesMap.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
						.forEachOrdered(x -> sortedNamesPricesMap.put(x.getKey(), x.getValue()));	
		return sortedNamesPricesMap;
	}
	
	public Map<String, Float> sortByRanking() {
		List<String> productNames = this.getproductNames();
		List<Float> productRankings = this.getProductRankings();
		Map<String, Float> unsortedNamesRankingsMap = new HashMap<String, Float>();
		LinkedHashMap<String, Float> sortedNamesRankingsMap = new LinkedHashMap<String, Float>();
		for (int i = 0; i < productNames.size(); i++) {
			unsortedNamesRankingsMap.put(productNames.get(i), productRankings.get(i));
		}
		unsortedNamesRankingsMap.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
						.forEachOrdered(x -> sortedNamesRankingsMap.put(x.getKey(), x.getValue()));	
		return sortedNamesRankingsMap;
	}
	
	public boolean assertOrderedPrices() {
		Map<String, Integer> productByPrice = this.sortByPrice();
		boolean isOrderedByPrice = false;
		Integer[] prices = productByPrice.values().toArray(new Integer[productByPrice.size()]);
		for (int i = 0; i < prices.length-1; i++) {
			if (prices[i] > prices[i+1]) {
				isOrderedByPrice = true;
			} else {
				isOrderedByPrice = false;
				return isOrderedByPrice;
			}
		}
		return isOrderedByPrice;
	}
	
	public void recommendProduct() {
		Map<String, Float> productByRanking = this.sortByRanking();
		int count = 1;
		for (Map.Entry<String, Float> entry : productByRanking.entrySet()) {
			if (count == 1) {
				log.info("Recommended product by ranking: " + entry.getKey());
				return;
			}		
		}
	}

}
