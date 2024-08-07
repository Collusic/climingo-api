package com.climingo.climingoApi.upload;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ThumbnailExtractor {

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    @Value("${ffmpeg.ffmpeg-path}")
    private String ffmpegPath;

    @Value("${ffmpeg.ffprobe-path}")
    private String ffprobePath;

    @PostConstruct
    public void init() {
        try {
            ffmpeg = new FFmpeg(ffmpegPath);
            ffprobe = new FFprobe(ffprobePath);
            log.debug("VideoFileUtils init complete.");
        } catch (IOException e) {
            log.error("VideoFileUtils init fail.", e);
        }
    }

    public File extractImage(String videoPath) throws IOException {
        File thumbnail = new File("thumbnail_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
            + ".jpg");

        try {
            FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true)
                .setInput(videoPath)
                .addExtraArgs("-ss", "00:00:02")
                .addOutput(thumbnail.getAbsolutePath())
                .setFrames(100)
                .addExtraArgs("-update", "1")
                .done();
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();
        } catch (Exception e) {
            log.error("Error extracting thumbnail from " + videoPath, e);
        }
        return thumbnail;
    }
}
