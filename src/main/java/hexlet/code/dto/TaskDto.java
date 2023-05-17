package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @NotNull
    @NotBlank
    @Size(min = 1)
    private String name;

    @Lob
    private String description;

    private Long executorId;

    @NotNull
    private Long taskStatusId;

    private Set<Long> labelIds;

}
