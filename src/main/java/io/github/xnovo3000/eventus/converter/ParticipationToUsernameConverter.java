package io.github.xnovo3000.eventus.converter;

import io.github.xnovo3000.eventus.entity.Participation;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParticipationToUsernameConverter extends AbstractConverter<List<Participation>, List<String>> {

    @Override
    protected List<String> convert(List<Participation> source) {
        return source != null ?
                source.stream().map(participation -> participation.getUser().getUsername()).toList() :
                List.of();
    }

}