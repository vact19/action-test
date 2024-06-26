package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.external.file.FileIOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpRange;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class VideoService {
    @Value("${file.upload.path}")// yml 설정파일
    private String fileUploadPath;

    private UrlResource video = null;

    public ResourceRegion getVideoResource(String filePath, Optional<HttpRange> httpRangeoptional) {
        String fullFilePath = "file:///"+fileUploadPath + filePath;
        ResourceRegion resourceRegion;
        long videoContentLength;
        final long chunkSize = 1000000L;

        try {
            video = new UrlResource(fullFilePath);
            videoContentLength = video.contentLength();
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_READ, e);
        }
        if (httpRangeoptional.isPresent()) {
            HttpRange httpRange = httpRangeoptional.get();
            long start = httpRange.getRangeStart(videoContentLength);
            long end = httpRange.getRangeEnd(videoContentLength);
            long length = Long.min(chunkSize, end - start + 1);
            resourceRegion = new ResourceRegion(video, start, length);
        } else {
            long length = Long.min(chunkSize, videoContentLength);
            resourceRegion = new ResourceRegion(video, 0, length);
        }
        return resourceRegion;
    }

    public MediaType getMediaType() {
        Optional<MediaType> opMediaType = MediaTypeFactory.getMediaType(video);
        return opMediaType.orElse(MediaType.APPLICATION_OCTET_STREAM);
    }
}
