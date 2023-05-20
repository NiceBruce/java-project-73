package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Label createLabel(LabelDto dto) {
        final Label label = new Label();
        label.setName(dto.getName());
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(long id, LabelDto dto) {
        Label labelToUpdate = labelRepository.findById(id).get();
        labelToUpdate.setName(dto.getName());
        return labelRepository.save(labelToUpdate);
    }

    @Override
    public Label getCurrentLabel(long id) {
        return labelRepository.findById(id).get();
    }

    public Set<Label> getLabels(Set<Long> labelIDs) {
        return labelIDs.stream()
                .map(labelsId -> labelRepository.findById(labelsId).get())
                .collect(Collectors.toSet());
//        return LabelIDs.stream()
//                .map(labelRepository::getById)
//                .collect(Collectors.toSet());
    }
}
