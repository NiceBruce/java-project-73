package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

import java.util.Set;

public interface LabelService {
    Label createLabel(LabelDto dto);

    Label updateLabel(long id, LabelDto dto);

    Label getCurrentLabel(long id);

    Set<Label> getLabels(Set<Long> labelIDs);
}
