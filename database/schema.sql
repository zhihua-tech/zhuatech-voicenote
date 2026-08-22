-- Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/
CREATE DATABASE IF NOT EXISTS zhuatech_voicenote DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE zhuatech_voicenote;

CREATE TABLE voice_note (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  audio_object_key VARCHAR(500) NOT NULL,
  duration_seconds INT NOT NULL,
  language_code VARCHAR(32) NOT NULL DEFAULT 'zh-CN',
  consent_confirmed TINYINT(1) NOT NULL DEFAULT 0,
  processing_status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
  summary TEXT NULL,
  retention_until DATETIME NULL,
  created_by BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_voice_note_status (processing_status),
  INDEX idx_voice_note_creator (created_by, created_at)
);

CREATE TABLE voice_note_segment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  note_id BIGINT NOT NULL,
  start_millis BIGINT NOT NULL,
  end_millis BIGINT NOT NULL,
  speaker_label VARCHAR(80) NULL,
  transcript TEXT NOT NULL,
  confidence DECIMAL(5,4) NULL,
  CONSTRAINT fk_segment_note FOREIGN KEY (note_id) REFERENCES voice_note(id) ON DELETE CASCADE,
  INDEX idx_segment_note_time (note_id, start_millis)
);

CREATE TABLE voice_note_action (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  note_id BIGINT NOT NULL,
  task_content VARCHAR(500) NOT NULL,
  owner_name VARCHAR(100) NULL,
  due_at DATETIME NULL,
  task_status VARCHAR(32) NOT NULL DEFAULT 'TODO',
  CONSTRAINT fk_action_note FOREIGN KEY (note_id) REFERENCES voice_note(id) ON DELETE CASCADE,
  INDEX idx_action_note (note_id, task_status)
);

