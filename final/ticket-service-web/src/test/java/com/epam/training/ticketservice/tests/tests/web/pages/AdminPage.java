package com.epam.training.ticketservice.tests.tests.web.pages;

import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.tests.tests.web.pages.support.CustomFluentPage;
import org.fluentlenium.core.annotation.PageUrl;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
//TODO this file was fine but now its a mess because of the stupid stale element thing
@PageUrl("/admin/")
public class AdminPage extends CustomFluentPage {
    @FindBy(css = "button[id='movies-tab']")
    protected FluentWebElement movieTab;
    @FindBy(css = "#movies button")
    protected FluentWebElement movieAddButton;

    @FindBy(css = "button[id='rooms-tab']")
    protected FluentWebElement roomTab;
    //TODO the delete buttons are <a> for some reason whereas these are buttons so its coincidental thatthis works, needs mroe specific filtering.
    //@FindBy(css = ".tab-pane.show button")
    @FindBy(css = "#rooms button")
    protected FluentWebElement roomAddButton;

    //TODO assert above buttons are the visible active buttons
    @FindBy(css = ".tab-pane.show button")
    protected FluentWebElement activePaneAddButton; //TODO currently assumes single, wont work for example with user tab


    @FindBy(css = ".show .modal-body input:not(:disabled)") //TODO see stale element hack
    protected FluentList<FluentWebElement> modalFields;
    @FindBy(css = ".show .modal-dialog button[type=submit]") //TODO see stale element hack
    protected FluentWebElement modalSubmit; //TODO do these get dynamically updated?

    //TODO the not has a is excluding the actions column
    //TODO problem is the row gets added in its proper sorted position in the original library
    @FindBy(css = ".tab-pane.show tbody tr:last-child td:not(:has(a))") //TODO again, doesnt handle multiple perpage
    protected FluentList<FluentWebElement> lastRowCells;

    @FindBy(css = ".tab-pane.show tbody tr:last-child")
    protected FluentWebElement lastRow;


    @FindBy(css = ".tab-pane.show .page-item:nth-last-child(2)")
    protected FluentWebElement lastPageButton; //The one before the next button //TODO better selector? involve not .next?
    @FindBy(css = ".tab-pane.show tr:last-child .delbutton")
    private FluentWebElement lastDeleteButton;

    @FindBy(css = "h1")
    protected FluentWebElement header;

    public void assertLoggedIn() {
        assertThat(header.text())
            .isEqualTo("Welcome to the Admin Page!");
    }

    public AdminPage navMovie(){
        movieTab.click();
        await()
            .atMost(3, TimeUnit.SECONDS)
            .until(movieAddButton)
            .clickable();
        return this;
    }
    public AdminPage navRoom(){
        roomTab.click();
        await()
            .atMost(3, TimeUnit.SECONDS)
            .until(roomAddButton)
            .clickable();
        return this;
    }

    public AdminPage fillModal(String... fields){
        //TODO stale element hack
        //modalSubmit = find(".show .modal-dialog button[type=submit]").first();
        //modalFields = find(".show .modal-body input:not(:disabled)");
        //assertThat(modalSubmit.clickable()).isTrue();
        for(int i = 0; i < fields.length; i++){
            //modalFields.get(i).write(fields[i]);
            int finalI = i;
            staleHack(d -> {
                var el = d.findElements(By.cssSelector(".show .modal-body input:not(:disabled)")).get(finalI);
                el.clear();
                el.sendKeys(fields[finalI]);
                return true;
            });
        }
        return this;
    }

    public AdminPage addMovie(MovieDto m){
        movieAddButton.click();
        await()
            .atMost(5, TimeUnit.SECONDS)
            .until(modalSubmit)
            .clickable();
        fillModal(m.getTitle(), m.getGenre(), String.valueOf(m.getRuntime()));
        modalSubmit.click(); //TODO switch to form submission?
        return this;
    }

    public AdminPage deleteLast(){
        goToLastPage();
        lastDeleteButton.click();
        await().until(modalSubmit).clickable();
        modalSubmit.click(); // Delete //TODO
        return this;
    }

    public AdminPage addRoom(RoomDto r){
        roomAddButton.click();
        await()
                .atMost(5, TimeUnit.SECONDS)
                .until(modalSubmit)
                .clickable();
        fillModal(r.getName(),
            String.valueOf(r.getRowCount()),
            String.valueOf(r.getColCount()));
        modalSubmit.click(); //TODO switch to form submission?
        await().explicitlyFor(500, TimeUnit.MILLISECONDS); //TODO wait until dialog disappears
        return this;
    }

    public AdminPage assertLastRow(String... vals){
        goToLastPage();
        //TODO look into what the fluentlenium assertions add
        //assertThat(lastRowCells.stream().map(FluentWebElement::text)).containsExactly(vals);
        final String[][] data = {new String[0]};
        staleHack(d -> {
            data[0] = d.findElements(By.cssSelector(".tab-pane.show tbody tr:last-child td:not(:has(a))"))
                    .stream().map(WebElement::getText).toArray(String[]::new);
            return true;
        });
        assertThat(data[0]).containsExactly(vals);
        return this;
    }

    public AdminPage assertLastRowNot(String... vals){
        goToLastPage();
        //TODO look into what the fluentlenium assertions add
        final String[][] data = {new String[0]};
        staleHack(d -> {
            data[0] = d.findElements(By.cssSelector(".tab-pane.show tbody tr:last-child td:not(:has(a))"))
                .stream().map(WebElement::getText).toArray(String[]::new);
            return true;
        });
        assertThat(data[0]).doesNotContain(vals);
        //assertThat(lastRowCells.stream().map(FluentWebElement::text).toArray()).doesNotContain(vals); //TODO whats the corect assert condtion here?
        return this;
    }

    public void staleHack(Function<WebDriver, Boolean> f){
        //TODO cant figure out why it keeps being stale
        // https://stackoverflow.com/questions/12967541/how-to-avoid-staleelementreferenceexception-in-selenium
        new WebDriverWait(getDriver(), Duration.of(5, ChronoUnit.SECONDS))
                .ignoring(StaleElementReferenceException.class)
                .until(f);
    }

    private AdminPage goToLastPage() {
        staleHack((d) -> { //lastPageButton.click()
            d.findElement(By.cssSelector(".tab-pane.show .page-item:nth-last-child(2)")).click();
            return true;
        });
        //TODO wait? for what?
        //await().atMost(1, TimeUnit.SECONDS).until()
        await().explicitlyFor(500, TimeUnit.MILLISECONDS);
        return this;
    }

    public String[] getLastRowValues() {
        final String[][] data = {new String[0]};
        staleHack(d -> {
            data[0] = d.findElements(By.cssSelector(".tab-pane.show tbody tr:last-child td:not(:has(a))"))
                    .stream().map(WebElement::getText).toArray(String[]::new);
            return true;
        });
        return data[0];

/*        return lastRowCells
            .stream()
            .map(FluentWebElement::text)
            .toArray(String[]::new);*/
    }

    public AdminPage editLastRow(String[] newVals) {
        goToLastPage();
        await().until(lastRow).clickable();
        lastRow.click();
        await().until(modalSubmit).clickable();
        fillModal(newVals);
        modalSubmit.click();
        await().explicitlyFor(1, TimeUnit.SECONDS);
        return this;
    }
}
