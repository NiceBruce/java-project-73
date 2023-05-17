package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "labels")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Label {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1)
    private String name;

    @ManyToMany(mappedBy = "labels")
    @JsonIgnore
    private Set<Task> tasks;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;
}
