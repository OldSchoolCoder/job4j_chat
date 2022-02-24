package ru.job4j.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.job4j.domain.Room;
import ru.job4j.dto.RoomDTO;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomDTO toDTO(Room room);
}
