import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.screening.ScreeningCrudService;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.screening.model.ScreeningMapper;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.tests.support.ExceptionHolderImpl;
import io.cucumber.java.After;
import io.cucumber.java.ExceptionHolder;
import io.cucumber.java.ExceptionWatchingStepDefs;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class ScreeningStepDefs implements ExceptionWatchingStepDefs {
    private final ScreeningCrudService ss;
    private final ExceptionHolderImpl eh;
    private final ScreeningMapper mapper;

    private Object lastResult;

    //TODO can I move these to a common class?
    @Override
    public ExceptionHolder getExceptionHolder() {
        return eh;
    }

    @After
    public void clearAfterScenario(){
        eh.clearException(); // TODO shouldnt this be handled by the exceptionholderimpl being @ScenarioScoped?
        lastResult = null;
    }

    public void assertScreening(String title, String room, Instant time){
        var key = mapper.dtoToAlternateKey(new ScreeningDto(title, room, time));
        assertThat(ss.getByAlternateKey(key).get())
                .usingRecursiveComparison()
                .isEqualTo(new ScreeningDto(title, room, time));
    }

    @When("I attempt to create a screening of {string} in {string} at -{zonedDateTime}-")
    public void iAttemptToCreateAScreeningOfInAtLocalstarttime(String movie, String room, ZonedDateTime time) {
        ss.create(new ScreeningDto(movie, room, time.toInstant()));
    }

    @Then("the screening for {string} in {string} at {zonedDateTime} exists")
    public void theScreeningForInAtExists(String movie, String room, Instant time) {
        try {
            ss.create(new ScreeningDto(movie, room, time));
        } catch (AlreadyExistsException ignored) {} // Don't need to do anything special
        assertScreening(movie, room, time);
    }

    /*@And("the screening for {string} in {string} is assigned an id")
    public void theScreeningForInIsAssignedAnId(String movie, String room) {

    }*/
}
