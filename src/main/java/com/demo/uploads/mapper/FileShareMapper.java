package com.demo.uploads.mapper;

import com.demo.uploads.dto.FileShareDto;
import com.demo.uploads.model.SharedFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileShareMapper {

    @Mapping(target = "shareIdentifier", source = "identifier")
    @Mapping(target = "fileName", source = "name")
    FileShareDto fileToDto(SharedFile sf);

    List<FileShareDto> filesToDtos(List<SharedFile> sharedFiles);
}
