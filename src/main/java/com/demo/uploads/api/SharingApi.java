package com.demo.uploads.api;

import com.demo.uploads.dto.FileSharesDto;
import com.demo.uploads.dto.SharePayload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "sharing", tags = { "sharing" })
public interface SharingApi {

    @ApiOperation(value = "Share file with given identifier to user with given email")
    void share(SharePayload dto);

    @ApiOperation(value = "Show files shared with authenticated user")
    FileSharesDto showAvailableShares();
}
