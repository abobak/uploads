package com.demo.uploads.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
