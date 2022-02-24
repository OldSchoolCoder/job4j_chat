package ru.job4j.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.job4j.domain.Role;
import ru.job4j.dto.RoleDTO;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO toDTO(Role role);
}
