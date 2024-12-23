package com.epam.training.ticketservice.lib.pricing;

import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.lib.pricing.persistence.*;
import com.epam.training.ticketservice.lib.pricing.persistence.model.TicketPriceDto;
import com.epam.training.ticketservice.lib.room.persistence.RoomCrudRepository;
import com.epam.training.ticketservice.lib.screening.ScreeningCrudService;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.screening.persistence.ScreeningCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {
    private final BasePriceRepository br;
    private final RoomSurchargeRepository rr;
    private final MovieSurchargeRepository mr;
    private final ScreeningSurchargeRepository sr;
    private final ScreeningCrudService scs;
    private final SurchargeRepository surr;

    private final RoomCrudRepository rcr;
    private final MovieCrudRepository mcr;
    private final ScreeningCrudRepository scr;

    @Override
    @Transactional //TODO is findall transactional or not?
    public Integer getBasePrice() {
        return br.findAll().stream().findFirst().get().getBasePrice(); //TODO
    }

    public List<Surcharge> getRoomSurcharges(String rn){
        return rr.findByRoom_Name(rn).stream()
                .map(RoomSurchargeMapInfo::getSurcharge)
                .toList();
    }

    public List<Surcharge> getMovieSurcharges(String movie) {
        return mr.findByMovie_Title(movie).stream()
                .map(MovieSurchargeMapInfo::getSurcharge)
                .toList();
    }

    public List<Surcharge> getScreeningSurcharges(ScreeningDto s) {
        return sr.findByScreening(scs.getMapper().dtoToEntity(s)).stream()
                .map(ScreeningSurchargeMapInfo::getSurcharge)
                .toList();
    }

    @Override
    public Integer getPrice(TicketPriceDto t) {
        var screening = t.getScreening();
        var totalSurcharge = Stream.of(
                        getScreeningSurcharges(screening),
                        getMovieSurcharges(screening.getMovieTitle()),
                        getRoomSurcharges(screening.getRoomName())
                ).flatMap(List::stream)
                .map(Surcharge::getPrice)
                .reduce(0, Integer::sum);
        return Math.toIntExact(t.seatCount) * (getBasePrice() + totalSurcharge);
    }

    @Override
    public void updateBasePrice(Integer i) {
        br.updateBasePriceBy(i);
    }

    @Override
    public void createPriceComponent(String name, Integer price) {
        surr.save(new Surcharge(name, price));
    }

    @Override
    public void attachPriceComponentToRoom(String name, String roomName) {
        var surcharge = surr.findByName(name).get();
        var room = rcr.getReferenceById(roomName);
        rr.save(new RoomSurchargeMap(null, surcharge, room));
    }

    @Override
    public void attachPriceComponentToMovie(String name, String movieName) {
        var surcharge = surr.findByName(name).get();
        var movie = mcr.getReferenceById(movieName);
        mr.save(new MovieSurchargeMap(null, surcharge, movie));
    }

    @Override
    public void attachPriceComponentToScreening(String name, ScreeningDto s) {
        var surcharge = surr.findByName(name).get();
        var screening = scr.getReferenceById(s.getId());
        sr.save(new ScreeningSurchargeMap(null, surcharge, screening));
    }


}
