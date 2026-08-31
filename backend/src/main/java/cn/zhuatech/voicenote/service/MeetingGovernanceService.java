/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.voicenote.service;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** 企业会议录音治理，兼顾知情同意、信息安全、保留策略和行动闭环。 */
@Service
public class MeetingGovernanceService {
    public Decision govern(Request request) {
        boolean consentComplete = request.participantCount() > 0
                && request.consentCount() == request.participantCount();
        int actionOwnerCoverage = request.actionItemCount() == 0 ? 100
                : Math.min(100, request.assignedActionItemCount() * 100 / request.actionItemCount());
        boolean recordingAllowed = consentComplete;
        boolean exportAllowed = consentComplete
                && (!request.sensitiveTopic() && request.externalParticipantCount() == 0
                || request.exportApproved());
        int effectiveRetentionDays = request.legalHold()
                ? Math.max(3650, request.retentionDays()) : request.retentionDays();
        String route = !consentComplete ? "BLOCKED_CONSENT"
                : !exportAllowed ? "EXPORT_REVIEW"
                : actionOwnerCoverage < 100 ? "ACTION_OWNER_FOLLOWUP" : "GOVERNED";
        List<String> controls = new ArrayList<>();
        if (!consentComplete) controls.add("补齐全部参会人录音知情同意");
        if (request.sensitiveTopic()) controls.add("敏感片段分级存储并限制转写导出");
        if (request.externalParticipantCount() > 0) controls.add("外部参会人仅访问授权纪要版本");
        if (actionOwnerCoverage < 100) controls.add("行动项必须补齐责任人与截止日期");
        if (request.legalHold()) controls.add("法律保全期间禁止删除原始录音与审计记录");
        return new Decision(request.noteId(), route, recordingAllowed, exportAllowed,
                actionOwnerCoverage, effectiveRetentionDays, List.copyOf(controls));
    }

    public record Request(@NotBlank String noteId, @Min(1) int participantCount,
                          @Min(0) int consentCount, @Min(0) int externalParticipantCount,
                          boolean sensitiveTopic, @Min(0) int actionItemCount,
                          @Min(0) int assignedActionItemCount,
                          @Min(1) @Max(3650) int retentionDays,
                          boolean exportApproved, boolean legalHold) {}

    public record Decision(String noteId, String route, boolean recordingAllowed,
                           boolean exportAllowed, int actionOwnerCoverage,
                           int effectiveRetentionDays, List<String> controls) {}
}
