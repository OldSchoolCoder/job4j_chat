package ru.job4j.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.job4j.domain.Person;
import ru.job4j.dto.PersonDTO;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDTO toDTO(Person person);
}
