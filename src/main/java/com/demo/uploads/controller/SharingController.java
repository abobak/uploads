package com.demo.uploads.controller;

import com.demo.uploads.api.SharingApi;
import com.demo.uploads.dto.FileShareDto;
import com.demo.uploads.model.User;
import com.demo.uploads.security.UserSecurityHelper;
import com.demo.uploads.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SharingController implements SharingApi {

    private final UserSecurityHelper userSecurityHelper;

    private final FilesService filesService;

    @PostMapping("/api/share")
    public void share(FileShareDto dto) {
        User currentUser = userSecurityHelper.getCurrentUser();
        filesService.shareWithOtherUser(dto.getShareIdentifier(), dto.getSharedWith(), currentUser);
    }
}
