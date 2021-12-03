package fun.onysakura.algorithm.kits.oa;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Slf4j
public class Debug {


    private static final ChromiumDriver DRIVER = getDriver();
    // 最大等待 30 秒，每 1 秒检测一次
    private static final WebDriverWait WAIT = new WebDriverWait(DRIVER, Duration.ofSeconds(30), Duration.ofSeconds(1));

    public static void main(String[] args) throws Exception {
        baiduSearch("Selenium Headless Browser").forEach(element -> {
            element.findElements(By.cssSelector("h3 a")).forEach(item -> {
                log.info(item.getText());
            });
        });
    }

    public static List<WebElement> baiduSearch(String keyword) {
        DRIVER.get("https://www.baidu.com");
        WAIT.until(ExpectedConditions.elementToBeClickable(By.id("kw"))).sendKeys(keyword + Keys.ENTER);
        log.debug("search done, waiting result");
        WAIT.until(driver -> {
            WebElement element = DRIVER.findElement(By.xpath("//*[@id=\"tsn_inner\"]/div[2]/span"));
            if (element != null) {
                log.info(element.getText());
                return true;
            }
            return false;
        });
        return WAIT.until(driver -> {
            List<WebElement> elements = driver.findElements(By.cssSelector(".result"));
            log.debug("result count: {}", elements.size());
            if (elements.size() > 5) {
                return elements;
            }
            return null;
        });
    }

    private static ChromiumDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "R:/Downloads/drivers/chromedriver86.exe");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        return new ChromeDriver(options);
    }
}
