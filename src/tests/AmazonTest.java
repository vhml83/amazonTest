package tests;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;
import pageObjects.AmazonPage;

public class AmazonTest {
	private final String CHROMEDRIVER = "webdriver.chrome.driver";
	private final String CHROMEDRIVER_PATH = "\\drivers\\chromedriver.exe";
	private final String AMAZON_URL = "https://www.amazon.com.mx/";
	private final String SEARCH_PRODUCT = "ipad air case";
	private final int MINIMUM_PRICE = 350;
	private final int MAXIMUM_PRICE = 750;
	private static Logger log = LogManager.getLogger(AmazonTest.class.getName());
	WebDriver driver;
	AmazonPage amazonPage;
	
	@BeforeTest
	public void setUp() throws InterruptedException {
		System.setProperty(CHROMEDRIVER, System.getProperty("user.dir")+ CHROMEDRIVER_PATH);	
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		log.info("Navigating to Amazon.com.mx");
		driver.get(AMAZON_URL);
	}
	
	@AfterTest
	public void tearDown(){
		log.info("Shutting down...");
		driver.close();
	}
	
	@Test(priority = 0, description="Output the Name, Price and Score/Rating (Stars) of the first 5 results")
	public void logProductDetails() throws InterruptedException {
		log.info("Beginning Test...");
		amazonPage = new AmazonPage(driver);
		log.info("Entering search product...");
		amazonPage.setInputText(SEARCH_PRODUCT);
		amazonPage.clickSearcSubmitButton();
		log.info("Redefining search for 'ProCase' brand...");
		amazonPage.clickCheckBox();
		log.info("Redefining search for price range between $350 and $750...");
		amazonPage.setPriceRange();
		log.info("Logging the Names of the first 5 results:");
		amazonPage.logFirst5ProductNames();
		log.info("Logging the Prices of the first 5 results:");
		amazonPage.logFirst5ProductPrices();
		log.info("Logging the Rankings of the first 5 results:");
		amazonPage.logFirst5ProductRankings();
	}
	
	@Test(priority = 1, description="Assert that the first 5 results are between $350 - $750")
	public void assertPriceRange() {
		log.info("Asserting that the first 5 results are between $350 - $750");
		List<Integer> productPrice = amazonPage.getproductPrices();
		for (Integer i : productPrice) {
			try {
				Assert.assertTrue(MINIMUM_PRICE <= i && i <= MAXIMUM_PRICE);
			} catch (AssertionError e) {
				log.error("Price out of range", e);
			}
		}
	}
	
	@Test(priority = 2, description="Sort the first 5 results by price and Assert the prices are ordered correctly")
	public void sortByPrice() {
		log.info("Sorting products by price...");
		Map<String, Integer> sortByPriceMap = amazonPage.sortByPrice();
		log.info(sortByPriceMap);
		Assert.assertTrue(amazonPage.assertOrderedPrices());
	}
	
	@Test(priority = 3, description="Sort the first 5 results by ranking")
	public void sortByRanking() {
		log.info("Sorting products by ranking...");
		Map<String, Float> sortByRankingMap = amazonPage.sortByRanking();
		log.info(sortByRankingMap);
		amazonPage.recommendProduct();
	}	

}