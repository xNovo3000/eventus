package io.github.xnovo3000.eventus.converter;

import io.github.xnovo3000.eventus.entity.Participation;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParticipationCountConverter extends AbstractConverter<List<Participation>, Integer> {

    @Override
    protected Integer convert(List<Participation> source) {
        return source != null ? source.size() : 0;
    }

}