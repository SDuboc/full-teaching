package selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LogoutTestSelenium {
    private static final Logger log = LogManager.getLogger();
    private WebDriver driver;

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "tests\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://localhost:5000/");
        Thread.sleep(3000);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
    }

    @Test
    public void logout() throws InterruptedException, ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        fazLogin();
        Thread.sleep(3_000);
        log.info("Iniciando logout");
        WebElement setinhaAbreLogout = driver.findElement(By.cssSelector("a #arrow-drop-down"));
        setinhaAbreLogout.click();

        WebElement btnLogout = driver.findElement(By.cssSelector("#logout-button"));
        btnLogout.click();
        log.info("logout sucesso");
    }

    private void fazLogin() throws InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        try {
            log.info("Realizando login");
            LoginTestSelenium loginTestSelenium = new LoginTestSelenium(driver);
            loginTestSelenium.loginTest();
            log.info("Login feito com sucesso");
        } catch (Exception ex) {
            log.error("Erro no método de fazer login: {}", ex.getMessage(), ex);
        }
    }
}
