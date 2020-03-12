package com.demo.uploads.api;

import com.demo.uploads.dto.SharePayload;
import com.demo.uploads.dto.FileSharesDto;

public interface SharingApi {

    void share(SharePayload dto);

    FileSharesDto showAvailableShares();
}
