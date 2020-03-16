package com.demo.uploads.controller;

import com.demo.uploads.api.SharingApi;
import com.demo.uploads.dto.FileSharesDto;
import com.demo.uploads.dto.SharePayload;
import com.demo.uploads.model.User;
import com.demo.uploads.security.UserSecurityHelper;
import com.demo.uploads.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SharingController implements SharingApi {

    private final UserSecurityHelper userSecurityHelper;

    private final FilesService filesService;

    @PostMapping("/api/share")
    public void share(@Valid @RequestBody SharePayload dto) {
        User currentUser = userSecurityHelper.getCurrentUser();
        filesService.shareWithOtherUser(dto.getShareIdentifier(), dto.getSharedWith(), currentUser);
    }

    @GetMapping("/api/file")
    public FileSharesDto showAvailableShares() {
        User currentUser = userSecurityHelper.getCurrentUser();
        return filesService.getAvailableFiles(currentUser);
    }
}
