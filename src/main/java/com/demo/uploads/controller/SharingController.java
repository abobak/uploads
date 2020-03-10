package com.demo.uploads.controller;

import com.demo.uploads.api.SharingApi;
import com.demo.uploads.dto.FileShareDto;
import com.demo.uploads.model.User;
import com.demo.uploads.security.UserSecurityHelper;
import com.demo.uploads.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SharingController implements SharingApi {

    private final UserSecurityHelper userSecurityHelper;

    private final FileService fileService;

    @PostMapping("/api/share")
    public void share(FileShareDto dto) {
        User currentUser = userSecurityHelper.getCurrentUser();
        fileService.shareWithOtherUser(dto.getShareIdentifier(), dto.getSharedWith(), currentUser);
    }
}
