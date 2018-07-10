

    import org.openqa.selenium.*;
    import org.openqa.selenium.chrome.ChromeDriver;

    import java.util.ArrayList;
    import java.util.List;


    public class Main  {
        private static WebDriver driver;

        public static void main(String[] args) {

            System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
            driver = new ChromeDriver();

            driver.get("https://en.wikipedia.org/wiki/To_Kill_a_Dragon");

            //Два варіанта знаходження списку акторів
            //*1
//          List<WebElement> elements = driver.findElements(By.tagName("ul"));
//          List<WebElement> elements1 = elements.get(2).findElements(By.tagName("a"));

            //*2
            String elementSelector = "h2 + ul";
            WebElement castElement = driver.findElement(By.cssSelector(elementSelector));
            List<WebElement> elements = castElement.findElements(By.tagName("a"));

            for (WebElement element : elements) {
                //Відкриття кожної сторінки актора в окремій вкладці
                ((JavascriptExecutor) driver).executeScript("window.open('" + element.getAttribute("href") + "');");
            }

            //Перевірка на тещо посилання на фільм присутнє і йде після оголошення фільмографії
            ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
            for (int i = 1; i < tabs.size(); i++) {
                driver.switchTo().window(tabs.get(i));
                if(isPresent(By.id("Filmography")) || isPresent(By.id("Selected_filmography")))
                    if(isPresent(By.linkText("To Kill a Dragon"))) {
                        if((getPointOfFilmography() - getPointOfReference()) < 0)
                             System.out.println(driver.getTitle() + " has reference to movie 'To kill a Dragon'");
                         else
                             System.out.println(driver.getTitle() + " DONT HAVE reference to movie 'To kill a Dragon'");
                    }else
                        System.out.println(driver.getTitle() + " DONT HAVE reference to movie 'To kill a Dragon'");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            driver.quit();
        }

        // Перевірка на наявність елемента на сторінці
        private static boolean isPresent(By by){
            try{
                driver.findElement(by);
                return true;
            }catch (NoSuchElementException e){}

            return false;
        }

        // знаходить елемент з id filmography і повертає його кордианту по х,
        // потрібно для перевірки чи посилання на фільми йде після блоку фільмографія
        private static int getPointOfFilmography(){
            WebElement element = null;
            try{
                element = driver.findElement(By.id("Filmography"));
               }catch (NoSuchElementException e){}
            try{
                element = driver.findElement(By.id("Selected_filmography"));
                }catch (NoSuchElementException e){}

            return element != null ? element.getLocation().getX() : 0;
        }

        //Повертає кординату посилання на фільм
        private static int getPointOfReference(){
            WebElement element = null;
            try{
                element = driver.findElement(By.linkText("To Kill a Dragon"));
            }catch (NoSuchElementException e){}

            return element != null ? element.getLocation().getX() : 0;
        }
    }