package com.epam.training.ticketservice.lib.reservation;

import com.epam.training.ticketservice.lib.reservation.model.ReservationDto;
import com.epam.training.ticketservice.lib.reservation.model.ReservationMapper;
import com.epam.training.ticketservice.lib.reservation.model.SeatDto;
import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationCrudRepository;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.reservation.persistence.Seat;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.screening.model.ScreeningMapper;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.screening.persistence.ScreeningCrudRepository;
import com.epam.training.ticketservice.lib.ticket.TicketCrudServiceImpl;
import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import com.epam.training.ticketservice.lib.ticket.model.TicketMapper;
import com.epam.training.ticketservice.lib.ticket.persistence.TicketCrudRepository;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder.createConstraintHandler;

@Service
public class ReservationCrudServiceImpl extends CustomCrudServiceImpl<ReservationDto, Reservation, ReservationKey, ReservationMapper, ReservationCrudRepository> implements ReservationCrudService {
    public ReservationCrudServiceImpl(ReservationCrudRepository repo, ReservationMapper mapper) {
        super(repo, mapper);
    }

    ///TODO probably shouldnt be in here?
    @Autowired
    protected TicketCrudServiceImpl ts;
    @Autowired
    protected TicketCrudRepository tr;
    @Autowired
    protected TicketMapper tm;
    @Autowired
    protected ScreeningCrudRepository ss;
    @Autowired
    protected ScreeningMapper sm;
    @Autowired
    private ReservationCrudRepository reservationCrudRepository;
    @Autowired
    private ReservationMapper reservationMapper;

    @SneakyThrows
    @Override
    public ReservationKey keyFromStrings(String... s) {
        var ticketKey = ts.keyFromStrings(s[0]);
        var ticket = tr.findById(ticketKey).orElseThrow(() -> new NoSuchRecordException(ticketKey.toString()));
        var screeningKey = sm.dtoToAlternateKey(sm.dtoFromStrings(s[1], s[2], s[3]));
        var screening = ss.getByAlternateKey(Screening.class, screeningKey)
                .orElseThrow(() -> new NoSuchRecordException(screeningKey.toString())); //TODO alternatekeyfromstrings
        return new ReservationKey(ticket, screening, new Seat(Integer.parseInt(s[4]), Integer.parseInt(s[5])));
    }

    public int keyFromStringsSize(){
        return 6;
    }
    ///

    @Override
    protected ConstraintHandlerHolder<ReservationDto> getCreateHandler() {
        return createConstraintHandler(
                Map.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, Exceptions::throwAlreadyExists),
                null);
    }

    @Override
    protected ConstraintHandlerHolder<ReservationDto> getUpdateHandler() {
        return createConstraintHandler(
                null,
                Map.of("CHECK_UPDATE_HAS_ROWS", Exceptions::throwNotExist)
        );
    }

    @Override
    public void delete(@NonNull ReservationKey id) { //TODO see base class
        super.rawDelete(id, Exceptions::throwNotExistWithContext);
    }

    @Override
    public List<ReservationDto> findAllByTicketId(Long ticketId) {
        return repo.findByReservationFor_Booking_TicketId(ticketId).stream().map(reservationMapper::entityToDto).collect(Collectors.toList());
    }

    public void addSeat(@NonNull TicketDto t, @NonNull ScreeningDto s, int row, int col){
        create(new ReservationDto(t.getTicketId(), s, new SeatDto(row, col)));
    }

}