package ru.practicum.explorewithme.statsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.dto.hit.Stats;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hits")
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "Hit.getAllUnique",
        query = "SELECT app, uri, count (DISTINCT ip) as hits " +
            "FROM hits " +
            "WHERE timestamp BETWEEN ?1 AND ?2 " +
            "AND uri in ?3 " +
            "GROUP BY app, uri ",
        resultSetMapping = "Mapping.Stats"
    ),
    @NamedNativeQuery(
        name = "Hit.getAll",
        query = "SELECT app, uri, count (ip) as hits " +
            "FROM hits " +
            "WHERE timestamp BETWEEN ?1 AND ?2 " +
            "AND uri in ?3 " +
            "GROUP BY app, uri ",
        resultSetMapping = "Mapping.Stats"
    )
})
@SqlResultSetMapping(name = "Mapping.Stats",
    classes = @ConstructorResult(
        targetClass = Stats.class,
        columns = {
            @ColumnResult(name = "app"),
            @ColumnResult(name = "uri"),
            @ColumnResult(name = "hits", type = Long.class)
        }
    )
)
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id")
    private Long id;
    @Column
    private String app;
    @Column
    private String uri;
    @Column
    private String ip;
    @Column
    private LocalDateTime timestamp;
}
