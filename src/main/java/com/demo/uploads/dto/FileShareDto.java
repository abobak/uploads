package com.demo.uploads.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileShareDto {

    private String sharedWith;

    private String shareIdentifier;

}
