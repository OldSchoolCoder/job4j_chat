package ru.job4j.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.job4j.domain.Message;
import ru.job4j.dto.MessageDTO;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDTO toDTO(Message message);
}
