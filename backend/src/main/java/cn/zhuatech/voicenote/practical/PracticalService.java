/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.voicenote.practical;

import static cn.zhuatech.voicenote.practical.PracticalInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class PracticalService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "segments");
    var segments = new ArrayList<>(rows(r, "segments", 1, 300));
    Set<String> ids = new HashSet<>();
    for (var s : segments) {
      keys(s, "id", "startMs", "endMs", "speaker", "text");
      unique(ids, id(s, "id"));
      int start = integer(s, "startMs", 0, 86400000), end = integer(s, "endMs", 1, 86400000);
      if (start >= end) throw new IllegalArgumentException("字幕结束必须晚于开始");
      String speaker = text(s, "speaker", 80);
      if (speaker.contains("\n") || speaker.contains("\r"))
        throw new IllegalArgumentException("说话人不能换行");
      text(s, "text", 4000);
    }
    segments.sort(
        Comparator.<Map<String, Object>>comparingInt(s -> ((Number) s.get("startMs")).intValue())
            .thenComparingInt(s -> ((Number) s.get("endMs")).intValue())
            .thenComparing(s -> s.get("id").toString()));
    StringBuilder vtt = new StringBuilder("WEBVTT\n\n");
    List<Map<String, Object>> out = new ArrayList<>();
    int index = 0;
    for (var s : segments) {
      String line = s.get("speaker") + ": " + s.get("text");
      line =
          line.replace("\r\n", "\n")
              .replace('\r', '\n')
              .replaceAll("\n{2,}", "\n")
              .replace("&", "&amp;")
              .replace("<", "&lt;")
              .replace(">", "&gt;");
      int start = ((Number) s.get("startMs")).intValue(),
          end = ((Number) s.get("endMs")).intValue();
      vtt.append(++index)
          .append("\n")
          .append(vttTime(start))
          .append(" --> ")
          .append(vttTime(end))
          .append("\n")
          .append(line.strip())
          .append("\n\n");
      out.add(
          Map.of(
              "id",
              s.get("id"),
              "startMs",
              start,
              "endMs",
              end,
              "speaker",
              s.get("speaker"),
              "caption",
              line.strip()));
    }
    return Map.of("segments", out, "webvtt", vtt.toString(), "transcriptionPerformed", false);
  }
}
