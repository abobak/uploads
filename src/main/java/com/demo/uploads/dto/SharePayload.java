package com.demo.uploads.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharePayload {

    private String sharedWith;

    private String shareIdentifier;

}
