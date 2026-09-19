/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.voicenote.service;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class VoiceNoteService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public NoteResult process(NoteRequest request) {
        List<String> warnings = new ArrayList<>();
        if (request.durationSeconds() < 10) warnings.add("录音较短，摘要信息可能不完整");
        if (request.durationSeconds() > 7200) warnings.add("长录音建议按议题分段处理");
        if (!request.consentConfirmed()) warnings.add("未确认录音参与人知情授权，仅允许保存草稿");

        List<Segment> segments = List.of(
            new Segment("00:00", "陈经理", "本周先完成华东仓的库存接口联调，周三前给出测试结果。"),
            new Segment("00:18", "李雯", "我负责整理异常订单样本，并把字段映射表同步到项目空间。"),
            new Segment("00:39", "陈经理", "下周一安排客户验收预演，接口负责人和实施顾问都参加。")
        );
        List<ActionItem> actions = List.of(
            new ActionItem("完成华东仓库存接口联调", "陈经理", "周三", "进行中"),
            new ActionItem("整理异常订单样本和字段映射表", "李雯", "周二", "待开始"),
            new ActionItem("组织客户验收预演", "项目经理", "下周一", "待安排")
        );
        Map<String, Object> providerPayload = new LinkedHashMap<>();
        providerPayload.put("audioObjectKey", request.audioObjectKey());
        providerPayload.put("language", request.language());
        providerPayload.put("speakerDiarization", request.speakerDiarization());
        providerPayload.put("summaryModel", "deepseek-compatible");
        providerPayload.put("retentionDays", request.retentionDays());

        String status = request.consentConfirmed() ? "READY" : "DRAFT_ONLY";
        return new NoteResult(status, "华东仓接口联调周会", "团队明确了接口联调、异常样本整理和验收预演三项工作，关键节点集中在本周三及下周一。",
            List.of("仓储系统", "接口联调", "客户验收"), segments, actions, List.copyOf(warnings), providerPayload, "LOCAL_DEMO_PIPELINE");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record NoteRequest(
        @NotBlank String audioObjectKey,
        @Min(3) @Max(14400) int durationSeconds,
        @NotBlank String language,
        boolean speakerDiarization,
        boolean consentConfirmed,
        @Min(1) @Max(365) int retentionDays,
        @Size(max = 500) String contextHint
    ) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Segment(String timestamp, String speaker, String text) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ActionItem(String task, String owner, String dueDate, String status) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record NoteResult(String status, String title, String summary, List<String> topics,
                             List<Segment> segments, List<ActionItem> actionItems, List<String> warnings,
                             Map<String, Object> providerPayload, String executionMode) {}
}

