import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class complexChallengeDragAndDrop {

    @Test
    public void VerifyDragAndDropWorkingSuccessfully() {
        WebDriver driver = WebDriverManager.chromedriver().create();
        driver.manage().window().maximize();
        driver.get("http://www.dhtmlgoodies.com/scripts/drag-drop-custom/demo-drag-drop-3.html");

        System.out.println("1. Verify draggable box color change when we put in correct country box");
        System.out.printf("|%-20s| %-20s| %-20s| %-20s|%n", "Capital", "Country", "hex color", "Correct/Incorrect?");
        DragAndDrop(driver,"Stockholm","Sweden");
        DragAndDrop(driver,"Washington","United States");
        DragAndDrop(driver,"Oslo","Norway");
        DragAndDrop(driver,"Madrid","Spain");
        DragAndDrop(driver,"Copenhagen","Denmark");
        DragAndDrop(driver,"Seoul","South Korea");
        DragAndDrop(driver,"Rome","Italy");

        System.out.println("\n\n");
        System.out.println("Reset the dropzone");
        ResetDragAndDrop(driver);
        System.out.println("\n\n");

        System.out.println("2. Verify draggable box color not change when we put in incorrect country box");
        System.out.printf("|%-20s| %-20s| %-20s| %-20s|%n", "Capital", "Country", "hex color", "Correct/Incorrect?");
        DragAndDrop(driver,"Stockholm","Italy");
        DragAndDrop(driver,"Washington","Spain");
        DragAndDrop(driver,"Oslo","United States");
        DragAndDrop(driver,"Madrid","Sweden");
        DragAndDrop(driver,"Copenhagen","Norway");
        DragAndDrop(driver,"Seoul","Denmark");
        DragAndDrop(driver,"Rome","South Korea");

        System.out.println("\n\n");
        System.out.println("Reset the dropzone");
        ResetDragAndDrop(driver);
        System.out.println("\n\n");

        System.out.println("3. Verify correct + incorrect combinations");
        System.out.printf("|%-20s| %-20s| %-20s| %-20s|%n", "Capital", "Country", "hex color", "Correct/Incorrect?");
        DragAndDrop(driver,"Stockholm","Sweden");
        DragAndDrop(driver,"Madrid","Spain");
        DragAndDrop(driver,"Copenhagen","South Korea");
        DragAndDrop(driver,"Washington","United States");
        DragAndDrop(driver,"Seoul","Italy");
        DragAndDrop(driver,"Oslo","Norway");
        DragAndDrop(driver,"Rome","Denmark");
    }

    public void DragAndDrop(WebDriver driver, String DragFrom,String DragTo){

        //Explicit Wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //Locators
        By draggableBox = By.xpath("//div[@id='dropContent']/div[text()='"+DragFrom+"'][not(contains(@style,'display: none'))]");
        By dropZone = By.xpath("//div[@id='mainContainer']/div[@id='countries']/div[text()='"+DragTo+"']");
        By draggableBoxAtDropZone = By.xpath("//div[@id='countries']//div[text()='"+DragFrom+"']");

        // Grab draggable box by it's name and put that in country dropzone by sending it's name
        Actions act = new Actions(driver);
        act.clickAndHold(wait.until(ExpectedConditions.elementToBeClickable(draggableBox)))
                        .moveToElement(wait.until(ExpectedConditions.presenceOfElementLocated(dropZone)))
                .release().build().perform();

        //Verify that draggable box color change using hex color we decide it pass or fail
        var color = wait.until(ExpectedConditions.presenceOfElementLocated(draggableBoxAtDropZone)).getCssValue("background-color");
        String hexColor = Color.fromString(color).asHex();
        var passOrFail = hexColor.equals("#00ff00") ? "Correct" : "Incorrect";
        System.out.printf("|%-20s| %-20s| %-20s| %-20s|%n", DragFrom, DragTo, hexColor, passOrFail);
    }

    public void ResetDragAndDrop(WebDriver driver){

        //Explicit Wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //Locators
        By draggableBox = By.xpath("//div[@id='countries']//div[@class='dragableBox']");
        By dropZone = By.xpath("//div[@id='capitals']");

        //Get List of Draggable box and store in list
        var draggableBoxList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(draggableBox));
        Actions act = new Actions(driver);

        // Grab one by one box and put that in capitals dropzone
        for (var box :draggableBoxList) {
            act.clickAndHold(box).moveToElement(wait.until(ExpectedConditions.presenceOfElementLocated(dropZone)))
                    .release().build().perform();
        }
    }
}