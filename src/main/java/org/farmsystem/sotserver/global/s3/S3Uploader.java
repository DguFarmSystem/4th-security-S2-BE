package org.farmsystem.sotserver.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + originalFileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata));

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void delete(String imageUrl) {
        String bucketUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
        String key = imageUrl.replace(bucketUrl, "");

        log.info("Deleting S3 file: {}", key);
        amazonS3.deleteObject(bucket, key);
    }
}
