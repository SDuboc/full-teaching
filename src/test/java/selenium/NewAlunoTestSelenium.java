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

public class NewAlunoTestSelenium {
    private static final String EMAIL_ALUNO = "alunonovo@gmail.com";
    private static final String SENHA_ALUNO = "pass";
    private static final String NICK_NAME = "AlunoTestSelenium";
    private static final String PASSWORD = "P@ss1234";
    private static final String CURRENT_PASSWORD = "Pass";
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
    public void newUserTest() throws InterruptedException {
        WebElement btnSignUp = driver.findElement(By.cssSelector("#signUpButton"));
        btnSignUp.click();

        preencheFormulario();

        checkBoxCaptcha();
        Thread.sleep(3_000);

        driver.switchTo().defaultContent();
        WebElement submitSignUp = driver.findElement(By.cssSelector("#sign-up-btn"));
        submitSignUp.click();
    }

    private void checkBoxCaptcha() {
        try {
            log.info("Realizando captcha");
            WebElement iframe = driver.findElement(By.cssSelector("#login-modal  re-captcha > div > div > div > iframe"));
            driver.switchTo().frame(iframe);
            WebElement captcha = driver.findElement(By.cssSelector("#recaptcha-anchor"));
            captcha.click();
            log.info("Captach solucionado com sucesso!");
        } catch (Exception ex) {
            log.error("Erro ao realizar o captcha da página: {}", ex.getMessage(), ex);
        }
    }

    private void preencheFormulario() {
        try {
            log.info("Iniciando processo de preencher formulário de novo aluno.");
            WebElement email = driver.findElement(By.cssSelector("#email"));
            email.click();
            email.sendKeys(EMAIL_ALUNO);

            WebElement nickName = driver.findElement(By.cssSelector("#nickName"));
            nickName.click();
            nickName.sendKeys(NICK_NAME);

            WebElement password = driver.findElement(By.cssSelector("#password"));
            password.click();
            password.sendKeys(PASSWORD);

            WebElement confirmPassword = driver.findElement(By.cssSelector("#confirmPassword"));
            confirmPassword.click();
            confirmPassword.sendKeys(PASSWORD);
            log.info("Formulário de novo aluno preenchido com sucesso!");
        } catch (Exception ex) {
            log.error("Erro ao preencher o formulário de novo aluno: {}", ex.getMessage(), ex);
        }
    }
}
