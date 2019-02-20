
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Milica
 */
public class MainNGTest {

    public MainNGTest() {
    }

        public static int getLastDayOfMonth(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }    
    
    @Test
    public void testGetLastDayOfMonthMethod() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        assertEquals(getLastDayOfMonth(date), 28);
    }
    @Test
    public void testSearchResults() {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        int lastDayofMonth = 0;
        try {
            lastDayofMonth = getLastDayOfMonth(date);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        String currentDate = dateFormat.format(date);
        String endDate = currentDate.substring(0, 8) + String.valueOf(lastDayofMonth);

        driver.get("https://www.booking.com");
        driver.findElement(By.id("ss")).sendKeys("Belgrade");
        driver.findElement(By.className("xp__dates__checkin")).click();
        driver.findElement(By.xpath("//*[@data-date='" + currentDate + "']")).click();
        driver.findElement(By.xpath("//*[@data-date='" + endDate + "']")).click();
        driver.findElement(By.className("xp__guests__count")).click();
        for (int i = 2; i < 8; i++) {
            driver.findElement(By.xpath("//div[@class='sb-group__field sb-group__field-adults']//span[@class='bui-button__text'][contains(text(),'+')]")).click();
        }
        for (int i = 1; i < 4; i++) {
            driver.findElement(By.xpath("//div[@class='sb-group__field sb-group__field-rooms']//span[@class='bui-button__text'][contains(text(),'+')]")).click();
        }
        for (int i = 0; i < 1; i++) {
            driver.findElement(By.xpath("//div[contains(@class,'sb-group__field sb-group-children')]//span[@class='bui-button__text'][contains(text(),'+')]")).click();
        }
        Select childrenAge = new Select(driver.findElement(By.xpath("//select[@name='age']")));
        childrenAge.selectByValue("12");
        driver.findElement(By.xpath("//button[@type='submit']//span[contains(text(),'Search')]")).click();

        String foundTitle = driver.findElement(By.xpath("//h1[contains(text(),'Belgrade: 157 properties found')]")).getText();

        assertEquals(foundTitle.trim(), "Belgrade: 157 properties found");
    }

}
