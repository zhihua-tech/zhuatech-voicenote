/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.voicenote.controller;

import cn.zhuatech.voicenote.service.VoiceNoteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voicenotes")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class VoiceNoteController {
    private final VoiceNoteService service;

    public VoiceNoteController(VoiceNoteService service) {
        this.service = service;
    }

    @PostMapping("/process")
    public VoiceNoteService.NoteResult process(@Valid @RequestBody VoiceNoteService.NoteRequest request) {
        return service.process(request);
    }
}

