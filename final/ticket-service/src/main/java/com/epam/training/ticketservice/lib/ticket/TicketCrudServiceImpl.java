package com.epam.training.ticketservice.lib.ticket;

import com.epam.training.ticketservice.lib.reservation.ReservationCrudServiceImpl;
import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import com.epam.training.ticketservice.lib.ticket.model.TicketMapper;
import com.epam.training.ticketservice.lib.ticket.persistence.Ticket;
import com.epam.training.ticketservice.lib.ticket.persistence.TicketCrudRepository;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder.createConstraintHandler;

@Service
public class TicketCrudServiceImpl extends CustomCrudServiceImpl<TicketDto, Ticket, Long, TicketMapper, TicketCrudRepository> implements TicketCrudService {
    @Autowired // circular dep
    protected ReservationCrudServiceImpl rs;
    @Autowired
    private TicketCrudRepository ticketCrudRepository;

    public TicketCrudServiceImpl(@NonNull TicketCrudRepository repo, @NonNull TicketMapper mapper) {
        super(repo, mapper);
    }

    @Override
    public Long keyFromStrings(String... s) {
        return Long.parseLong(s[0]);
    }

    @Override
    public int keyFromStringsSize() {
        return 1;
    }

    @Override
    protected ConstraintHandlerHolder<TicketDto> getCreateHandler() {
        return createConstraintHandler(
                Map.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, Exceptions::throwAlreadyExists),
                null);
    }

    @Override
    protected ConstraintHandlerHolder<TicketDto> getUpdateHandler() {
        return createConstraintHandler(
                null,
                Map.of("CHECK_UPDATE_HAS_ROWS", Exceptions::throwNotExist)
        );
    }

    @Override
    public void delete(@NonNull Long id) { //TODO see base class
        super.rawDelete(id, Exceptions::throwNotExistWithContext);
    }

    @Override
    public Optional<TicketDto> get(@NonNull Long ticketId) {
        return super.get(ticketId).map(ticketDto -> fillReservation(ticketDto, ticketId));
    }

    public TicketDto fillReservation(@NonNull TicketDto ticketDto, Long ticketId){
        var reservations = rs.findAllByTicketId(ticketId);
        var list = ticketDto.getReservations();
        if(list == null){
            list = new LinkedList<>();
            ticketDto.setReservations(list);
        };
        list.addAll(reservations);
        return ticketDto;
    }

    @Override
    public List<TicketDto> list() {
        return super.list().stream().peek(ticketDto -> { //This approach results in looking up every item recursively
            fillReservation(ticketDto, mapper.dtoToEntity(ticketDto).getTicketId()); //TODO probably doing unnecessary work here
        }).toList();
    }

    /*
    //TODO this feels like a hack
    @Transactional
    public void setPrice(TicketDto t, Integer price) {
        var e = repo.findById(t.getTicketId()).get();
        e.setPaid(price);
        repo.save(e);
    }
    */
}