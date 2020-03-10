package com.demo.uploads.api;

import com.demo.uploads.dto.FileShareDto;
import com.demo.uploads.dto.FileSharesDto;

public interface SharingApi {

    void share(FileShareDto dto);

    FileSharesDto showAvailableShares(String fileIdentifier);
}
