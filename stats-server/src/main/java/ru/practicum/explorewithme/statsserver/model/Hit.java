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
        query = "SELECT apps.name AS app, uri, count (DISTINCT ip) AS hits " +
            "FROM hits " +
            "LEFT JOIN apps ON apps.app_id = hits.app_id " +
            "WHERE timestamp BETWEEN ?1 AND ?2 " +
            "AND uri IN ?3 " +
            "GROUP BY apps.name, uri ",
        resultSetMapping = "Mapping.Stats"
    ),
    @NamedNativeQuery(
        name = "Hit.getAll",
        query = "SELECT apps.name AS app, uri, count (ip) AS hits " +
            "FROM hits " +
            "LEFT JOIN apps ON apps.app_id = hits.app_id " +
            "WHERE timestamp BETWEEN ?1 AND ?2 " +
            "AND uri IN ?3 " +
            "GROUP BY apps.name, uri ",
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "app_id", nullable = false)
    private App app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
