package fun.onysakura.algorithm.kits.oa;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
public class OA {

    private static WebDriver DRIVER = null;
    private static WebDriverWait WAIT;
    private static String user;
    private static String password;

    public static void main(String[] args) {
        try {
            init();
            oa(user, password);
        } catch (Exception e) {
            log.warn("初始化失败", e);
        } finally {
            if (DRIVER != null) {
                DRIVER.quit();
            }
        }
    }

    public static void oa(String user, String password) throws Exception {
        DRIVER.get("https://oa.tydic.com/");
        WAIT.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginform")));
        log.info("login...");
        IntStream.range(0, DRIVER.findElement(By.id("userCode")).getAttribute("value").length()).forEach(i -> DRIVER.findElement(By.id("userCode")).sendKeys(Keys.BACK_SPACE));
        DRIVER.findElement(By.id("userCode")).sendKeys(user);
        DRIVER.findElement(By.id("pwd")).sendKeys(password);
        DRIVER.findElement(By.id("login")).click();
        WAIT.until(ExpectedConditions.visibilityOfElementLocated(By.id("PM"))).click();
        log.debug("点击工时填报");
        WAIT.until(ExpectedConditions.visibilityOfElementLocated(By.id("WF_M001_12_05"))).click();
        log.debug("点击填报工时");
        WAIT.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("iframe#WF_M001_12_05")));
        DRIVER.switchTo()
                .frame("WF_M001_12_05")
                .findElement(By.id("WF_M001_12_05_01_02"))
                .findElement(By.cssSelector("a"))
                .click();
        log.debug("进入填报页面");
        DRIVER.switchTo().defaultContent();
        WAIT.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("iframe#whCreate")));
        log.debug("进入填报页面");
        DRIVER.switchTo().frame("whCreate");
        int i = LocalDateTime.now().get(ChronoField.DAY_OF_MONTH);
        String day = String.format("%02d", i);
        log.debug("current day: " + day);
        WAIT.until(driver -> DRIVER.findElement(By.cssSelector("#whiDiv")))
                .findElements(By.cssSelector("table.innerDetailList>tbody>tr"))
                .forEach(tr -> {
                    WebElement dateElement = tr.findElement(By.xpath("td[2]"));
                    if (dateElement.getText().startsWith(day)) {
                        log.debug("find element: " + dateElement.getText());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        tr.findElement(By.xpath("td[13]/a[1]")).click();
                    }
                });
        Thread.sleep(1000);
        DRIVER.findElement(By.cssSelector(".btn-operate>input.btn-save")).click();
        WAIT.until(driver -> DRIVER.findElement(By.cssSelector(".destroy .sheetcon input.sheet-close"))).click();
    }

    private static void init() {
        Yaml yaml = new Yaml();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("config.yml");
        } catch (FileNotFoundException ignored) {
            log.warn("can't find config file, use default");
        }
        if (inputStream == null) {
            inputStream = OA.class.getClassLoader().getResourceAsStream("config.yml");
        }
        JSONObject json = yaml.loadAs(inputStream, JSONObject.class);
        JSONObject oa = json.getJSONObject("oa");
        user = Objects.requireNonNull(oa.getString("user"));
        password = Objects.requireNonNull(oa.getString("password"));
        log.info("login user: {}", user);
        JSONObject webDriver = json.getJSONObject("webDriver");
        String driverFilePath = webDriver.getString("filePath");
        boolean headless = Objects.requireNonNullElse(webDriver.getBoolean("headless"), true);
        int x = new Random().nextInt();
        switch (x) {
            case 1 -> System.out.println(1);
            case 2 -> System.out.println(2);
            default -> throw new RuntimeException("sad");
        }
        switch (Objects.requireNonNull(webDriver.getString("type"))) {
            case "chrome" -> {
                System.setProperty("webdriver.chrome.driver", driverFilePath);
                ChromeOptions options = new ChromeOptions();
                options.setHeadless(headless);
                DRIVER = new ChromeDriver(options);
            }
            case "edge" -> {
                System.setProperty("webdriver.edge.driver", driverFilePath);
                EdgeOptions options = new EdgeOptions();
                options.setHeadless(headless);
                DRIVER = new EdgeDriver(options);
            }
            case "firefox" -> {
                System.setProperty("webdriver.gecko.driver", driverFilePath);
                FirefoxOptions options = new FirefoxOptions();
                options.setHeadless(headless);
                DRIVER = new FirefoxDriver(options);
            }
            case "ie" -> {
                System.setProperty("webdriver.ie.driver", driverFilePath);
                InternetExplorerOptions options = new InternetExplorerOptions();
                DRIVER = new InternetExplorerDriver(options);
            }
            default -> throw new RuntimeException("Unknown browser type");
        }
        // 最大等待 30 秒，每 1 秒检测一次
        WAIT = new WebDriverWait(DRIVER, Duration.ofSeconds(30), Duration.ofSeconds(1));
    }

}
