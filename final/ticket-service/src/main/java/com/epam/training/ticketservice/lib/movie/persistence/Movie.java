package com.epam.training.ticketservice.lib.movie.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@NamedQuery(name="Movie.update", query="UPDATE Movie m SET m.genre = :genre, m.runtime = :runtime WHERE m.title = :title")
@NamedStoredProcedureQuery(name="Movie.update", procedureName="nonempty_update_by_key", parameters = {
        @StoredProcedureParameter(mode=ParameterMode.IN, name = "tablename", type = String.class), //TODO is there a decent solution to this? this is  hack for the varargs and relative const params
        @StoredProcedureParameter(mode=ParameterMode.IN, name = "titlename", type = String.class), //TODO is there a decent solution to this? this is  hack for the varargs and relative const params
        @StoredProcedureParameter(mode=ParameterMode.IN, name = "title", type = String.class),
        @StoredProcedureParameter(mode=ParameterMode.IN, name = "genrename", type = String.class),
        @StoredProcedureParameter(mode=ParameterMode.IN, name = "genre", type = String.class),
        @StoredProcedureParameter(mode=ParameterMode.IN, name = "runtimename", type = String.class),
        @StoredProcedureParameter(mode=ParameterMode.IN, name = "runtime", type = Integer.class),
})
@Table(name = "movie")
public class Movie {
    @Id
    public String title;

    @Column(nullable = false)
    public String genre;

    /**
     * Runtime in minutes.
     */
    @Column(nullable = false)
    public Integer runtime;
}
