package com.demo.uploads.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileSharesDto {

    private List<FileShareDto> myFiles = new LinkedList<>();

    private List<FileShareDto> sharedWithMe = new LinkedList<>();
}
