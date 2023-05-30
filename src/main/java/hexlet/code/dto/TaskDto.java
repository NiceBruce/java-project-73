package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @NotNull
    @NotBlank
    @Size(min = 1)
    private String name;

    private String description;

    private Long executorId;

    @NotNull
    private Long taskStatusId;

    private Set<Long> labelIds;

}
