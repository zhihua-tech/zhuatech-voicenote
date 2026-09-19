/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.voicenote;

import cn.zhuatech.voicenote.service.MeetingGovernanceService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class MeetingGovernanceServiceTests {
    private final MeetingGovernanceService service = new MeetingGovernanceService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void blocksRecordingWithoutCompleteConsent() {
        var result = service.govern(new MeetingGovernanceService.Request(
                "NOTE-01", 8, 7, 1, true, 3, 2, 180, false, false));
        assertThat(result.route()).isEqualTo("BLOCKED_CONSENT");
        assertThat(result.recordingAllowed()).isFalse();
        assertThat(result.exportAllowed()).isFalse();
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void governsApprovedMeetingAndLegalHold() {
        var result = service.govern(new MeetingGovernanceService.Request(
                "NOTE-02", 8, 8, 1, true, 3, 3, 180, true, true));
        assertThat(result.route()).isEqualTo("GOVERNED");
        assertThat(result.effectiveRetentionDays()).isEqualTo(3650);
    }
}
