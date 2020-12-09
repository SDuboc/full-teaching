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


public class ChangePasswordTestSelenium {
    private static final String SENHA = "pass";
    private static final String NICK_NAME = "AlunoTestSelenium";
    private static final String CURRENT_PASSWORD = "Pass";
    private static final String NEW_PASSWORD = "P@ass1234";
    private WebDriver driver;
    private static final Logger log = LogManager.getLogger();

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
    public void changePassword() throws InterruptedException, ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        fazLogin();
        Thread.sleep(3_000);
        entraPaginaSettings();
        preencheFormulario();
        submit();
    }

    private void submit() {
        try {
            WebElement btnSend = driver.findElement(By.cssSelector("#password-modal button"));
            btnSend.click();
        } catch (Exception ex) {
            log.error("Erro ao realizar submit do formulário de troca de senha: {}", ex.getMessage(), ex);
        }
    }

    private void preencheFormulario() {
        try {
            log.info("Preenchendo formulário de troca de senha.");
            WebElement currentPassword = driver.findElement(By.cssSelector("#inputCurrentPassword"));
            currentPassword.click();
            currentPassword.sendKeys(CURRENT_PASSWORD);

            WebElement newPassword = driver.findElement(By.cssSelector("#inputNewPassword"));
            newPassword.click();
            newPassword.sendKeys(NEW_PASSWORD);

            WebElement repeatPassword = driver.findElement(By.cssSelector("#inputNewPassword2"));
            repeatPassword.click();
            repeatPassword.sendKeys(NEW_PASSWORD);
            log.info("Formulário preenchido com sucesso");
        } catch (Exception ex) {
            log.error("Erro ao preencher formulário de troca de senha: {}", ex.getMessage(), ex);
        }
    }

    private void entraPaginaSettings() {
        try {
            log.info("Entrando na página de settings do usuário");
            WebElement btnSettings = driver.findElement(By.cssSelector("#settings-button"));
            btnSettings.click();

            WebElement btnChangePassword = driver.findElement(By.cssSelector("#sticky-footer-div ul > li:nth-child(4) > div> a"));
            btnChangePassword.click();
            log.info("Sucesso ao entrar na página de settings do usuário");
        } catch (Exception ex) {
            log.error("Erro ao tentar entrar na página de settings do usuário logado: {}", ex.getMessage(), ex);
        }
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
