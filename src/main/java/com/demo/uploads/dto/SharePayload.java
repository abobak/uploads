package com.demo.uploads.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharePayload {

    @NotNull
    private String sharedWith;

    @NotNull
    private String shareIdentifier;

}
