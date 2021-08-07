import org.codehaus.groovy.runtime.StackTraceUtils
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest

import java.time.Duration

class SignedS3Service {

    static String accessKey = ""
    static String secretKey = ""
    static String bucketName = ""

    static S3Presigner getS3Presigner() {
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials
                .create(accessKey, secretKey))
        S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_2)
                .build()
        println("Connection to S3 presigner made successfully..")
        return presigner
    }


    static String getUploadUrl(String key) {

        String contentType = "application/pdf"
        Integer expiryTime = 419
        String myURL = ""

        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build()

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expiryTime))
                    .putObjectRequest(objectRequest)
                    .build()

            PresignedPutObjectRequest presignedRequest = getS3Presigner().presignPutObject(presignRequest)
            myURL = presignedRequest.url().toString()

            if (myURL) {
                println("URL generated SuccessFully..")
            } else {
                println("Could not generate URL")
            }
        } catch (ex) {
            println("Could not generate URL :Exception Occurred.")

        }
        return myURL

    }


    static void getDownloadUrl(String key) {
        URL url = null
        Integer expiryTime = 419

        try {
            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build()
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expiryTime))
                    .getObjectRequest(getObjectRequest)
                    .build()
            PresignedGetObjectRequest presignedGetObjectRequest =
                    getS3Presigner().presignGetObject(getObjectPresignRequest)

            url = presignedGetObjectRequest.url()
            if (url) {
                println("Download URL has been generated Successfully..for Key-${key} & Bucket-${bucketName}")
                println(" URL "+url)
            } else {
                println("Could not generate URL..for Key-${key} & Bucket-${bucketName}")

            }

        } catch (ex) {

            println("Could not generate URL" + StackTraceUtils.deepSanitize(ex))
        }

    }


}


