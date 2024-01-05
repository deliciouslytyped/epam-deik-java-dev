package com.epam.training.ticketservice.presentation.web;

import com.epam.training.ticketservice.lib.movie.MovieCrudService;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.reservation.ReservationCrudService;
import com.epam.training.ticketservice.lib.reservation.model.SeatMapperImpl;
import com.epam.training.ticketservice.lib.room.RoomCrudService;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.screening.ScreeningCrudService;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.ticket.TicketCrudService;
import com.epam.training.ticketservice.lib.user.AdminAccountCrudService;
import com.epam.training.ticketservice.lib.user.UserAccountCrudService;
import com.epam.training.ticketservice.lib.user.model.AdminDto;
import com.epam.training.ticketservice.lib.user.model.UserCreationDto;
import com.epam.training.ticketservice.lib.user.model.UserDto;
import com.epam.training.ticketservice.support.CustomCrudService;
import com.epam.training.ticketservice.support.dto.DTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//TODO something weird is happening with mappings, / is mapped to index by default?

//TODO could split these into separate modules
//TODO write generic functionality for foreign key enumeration, service mapping
@Controller //TODO restcontroller?
@RequiredArgsConstructor
public class WebController implements InitializingBean {
    protected final UserAccountCrudService us;
    protected final AdminAccountCrudService as;
    protected final MovieCrudService ms;
    protected final RoomCrudService rs;
    protected final ScreeningCrudService ss;
    protected final ReservationCrudService res;
    protected final TicketCrudService ts;

    private static final String adminPage = "/admin/"; //TODO move to some kind of url structure enum or something for central config
    private static final String userPage = "/user/"; //TODO move to some kind of url structure enum or something for central config

    protected Map<String, CustomCrudService> entityWhitelist;


    @Override
    public void afterPropertiesSet() throws Exception {
        entityWhitelist = Map.of(
                "applicationadmin", as,
                "applicationuser", us,
                "movie", ms,
                "screening", ss,
                "room", rs,
                "reservations", res
        );
    }

    @ModelAttribute("admins")
    public List<AdminDto> admins() {
        return as.list();
    }
    @ModelAttribute("users")
    public List<UserDto> users() {
        return us.list();
    }

    @ModelAttribute("movies")
    public List<MovieDto> movies() {
        return ms.list();
    }

    //TODO dont do this, the query should run in the db, and move these to the service layer
    // these sets are used by the frontend for enumerated selection boxes
    @ModelAttribute("movieTitleSet")
    public Set<String> movieTitleSet() {
        return movies().stream().map(MovieDto::getTitle).collect(Collectors.toSet());
    }

    @ModelAttribute("rooms")
    public List<RoomDto> rooms() {
        return rs.list();
    }

    @ModelAttribute("roomNameSet")
    public Set<String> roomNameSet() {
        return rooms().stream().map(RoomDto::getName).collect(Collectors.toSet());
    }

    @ModelAttribute("screenings")
    public List<ScreeningDto> screenings() {
        return ss.list();
    }

    @GetMapping("/")
    public String getHomepage(Model m){
        return "index";
    }

    @GetMapping(adminPage)
    public String getAdminPage(Model m){
        return "admin";
    }

    //TODO server side validation or something on these, currenty we just expect the object creation to fail
    @PostMapping(adminPage)
    public ResponseEntity<?> createEntity(@RequestBody JsonModificationRequest e){
        if(e.getEntity().equals("seat")){
            return addSeatToReservation(e.getProperties());
        }
        var service = entityWhitelist.get(e.getEntity());
        var dto = service.getMapper().dtoFromJSON(e.getProperties()); //TODO needs more sanitization, probably a safeDtoFromJSON or SafeMapper or something, or something on the service layer.
        service.create(dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping(adminPage)
    public ResponseEntity<?> deleteEntity(@RequestBody JsonModificationRequest e){
        var service = entityWhitelist.get(e.getEntity());
        var dto = service.getMapper().dtoFromJSON(e.getProperties()); //TODO needs more sanitization, probably a safeDtoFromJSON or SafeMapper or something, or something on the service layer.
        service.delete(((DTO)dto).getKey()); //TODO hacked this in real quick, will break on everything other than movie and room
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<Object> addSeatToReservation(JsonNode e) {
        var seat = new SeatMapperImpl().dtoFromJSON(e.get("seat"));
        var screeningDto = ss.getMapper().dtoFromJSON(e.get("screening"));
        var ticketDto = ts.get(Long.parseLong(e.get("ticketid").textValue())).get();
        res.addSeat(ticketDto, screeningDto, seat.getRowIdx(), seat.getColIdx());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(adminPage + "register")
    public String registerAdmin(@RequestParam String username, @RequestParam String password){
        as.register(new UserCreationDto(username, password));
        return "redirect:" + adminPage ; //TODO "go back to the given page" mechanism?
    }

    @PostMapping(userPage + "register") //TODO this setup doesnt allow registering users from the admin page
    public String registerUser(@RequestParam String username, @RequestParam String password){
        us.register(new UserCreationDto(username, password));
        return "redirect:" + userPage;
    }
    @PutMapping(adminPage)
    public ResponseEntity<?> updateEntity(@RequestBody JsonModificationRequest e){
        var service = entityWhitelist.get(e.getEntity());
        var dto = service.getMapper().dtoFromJSON(e.getProperties()); //TODO needs more sanitization, probably a safeDtoFromJSON or SafeMapper or something, or something on the service layer.
        service.update(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK); //TODO best status code for update?
    }

    @GetMapping(userPage)
    public String getUserPage(){
        return "user";
    }
}
