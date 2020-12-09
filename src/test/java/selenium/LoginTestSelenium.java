package selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginTestSelenium {
    private static final String EMAIL = "student2@gmail.com";
    private static final String SENHA = "pass";
    private WebDriver driver;

    public LoginTestSelenium(WebDriver driver) {
        this.driver = driver;
    }

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
    public void loginTest() throws InterruptedException {
        WebElement btnLogin = driver.findElement(By.cssSelector("#navigation-bar > div > ul > li:nth-child(2) > a"));
        btnLogin.click();

        WebElement inputEmail = driver.findElement(By.cssSelector("#email"));
        inputEmail.click();
        inputEmail.sendKeys(EMAIL);

        WebElement inputSenha = driver.findElement(By.cssSelector("#password"));
        inputSenha.click();
        inputSenha.sendKeys(SENHA);

        WebElement submitLogin = driver.findElement(By.cssSelector("#log-in-btn"));
        submitLogin.click();
    }


}
