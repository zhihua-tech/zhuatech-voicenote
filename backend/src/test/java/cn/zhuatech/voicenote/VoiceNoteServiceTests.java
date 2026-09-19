/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.voicenote;

import cn.zhuatech.voicenote.service.VoiceNoteService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class VoiceNoteServiceTests {
    private final VoiceNoteService service = new VoiceNoteService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void createsStructuredNoteWhenConsentIsConfirmed() {
        var request = new VoiceNoteService.NoteRequest("demo/weekly-meeting.m4a", 1380, "zh-CN", true, true, 30, "仓储项目周会");
        var result = service.process(request);
        assertThat(result.status()).isEqualTo("READY");
        assertThat(result.segments()).hasSize(3);
        assertThat(result.actionItems()).hasSize(3);
        assertThat(result.executionMode()).isEqualTo("LOCAL_DEMO_PIPELINE");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void restrictsNoteWithoutRecordingConsent() {
        var request = new VoiceNoteService.NoteRequest("demo/private.m4a", 20, "zh-CN", false, false, 7, "");
        var result = service.process(request);
        assertThat(result.status()).isEqualTo("DRAFT_ONLY");
        assertThat(result.warnings()).anyMatch(item -> item.contains("知情授权"));
    }
}

