import org.codehaus.groovy.runtime.StackTraceUtils
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*

class S3Operations {


    static String accessKey = ""
    static String secretKey = ""
    static String bucketName = ""

    static void uploadObject(File file, String key) {

        try {

            S3Client s3Client = getAWSClient(accessKey, secretKey, bucketName)

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromFile(file))

            if (putObjectResponse.asBoolean()) {
                println("Object Pushed Successfully.")
            } else {
                println("Could not push object..")
            }

        } catch (ex) {
            println("Exception in uploading file [${key}] :" + StackTraceUtils.deepSanitize(ex))
        }

    }

    static void downloadObject(String key, String location) {

        try {
            S3Client s3Client = getAWSClient(accessKey, secretKey, bucketName)

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()

            ResponseInputStream<GetObjectResponse> getObjectResponse = s3Client.getObject(getObjectRequest)
            File file = new File(location)
            OutputStream os = new FileOutputStream(file)
            os.write(getObjectResponse.bytes)
            if (os.asBoolean()) {
                println("File saved to [${location}] Succesfully..")
            } else {
                println("Could not save file at [${location}] ..")
            }
        } catch (ex) {
            println("Exception in Downloading File [${key}] :" + StackTraceUtils.deepSanitize(ex))
        }

    }

    static void deleteObject(String key) {

        try {

            S3Client s3Client = getAWSClient(accessKey, secretKey, bucketName)

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()

            DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest)

            if (deleteObjectResponse.asBoolean()) {
                println("Object Deleted Successfully.")
            } else {
                println("Could not Delete object..")
            }

        } catch (ex) {
            println("Exception in Deleting file [${key}] :" + StackTraceUtils.deepSanitize(ex))
        }

    }

    static void getListObject(String prefix) {
        List nameList = []
        try {

            S3Client s3Client = getAWSClient(accessKey, secretKey, bucketName)

            ListObjectsV2Request listObjects = ListObjectsV2Request
                    .builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build()

            ListObjectsV2Response res = s3Client.listObjectsV2(listObjects)
            List<S3Object> objects = res.contents()
            nameList = objects.collect({ it.key() })

            if (nameList.asBoolean()) {
                println("Object Names Fetched Successfully.")

                nameList.each {
                    println(it)
                }

            } else {
                println("Could not Fetch Object Names")
            }

        } catch (ex) {
            println("Exception in Fetching files [${prefix}] :" + StackTraceUtils.deepSanitize(ex))
        }

    }


    static void moveObject(String fromKey, String toKey) {

        S3Client s3Client = getAWSClient(accessKey, secretKey, bucketName)

        try {
            CopyObjectRequest copyReq = CopyObjectRequest.builder()
                    .sourceBucket(bucketName)
                    .sourceKey(fromKey)
                    .destinationBucket(bucketName)
                    .destinationKey(toKey)
                    .build()
            CopyObjectResponse copyRes = s3Client.copyObject(copyReq)
            println("file Copied to [${toKey}] Successfully..")
            deleteObject(fromKey)
        } catch (ex) {
            println("Could not Move File from ${fromKey} To ${toKey} inside Bucket ${bucketName}")
        }

    }

    static void createDirectory(String directoryToCreate) {

        S3Client s3Client = getAWSClient(accessKey, secretKey, bucketName)

        String key = directoryToCreate
        int i = directoryToCreate.lastIndexOf("/")
        key = directoryToCreate.substring(0, i + 1)

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
        try {
            s3Client.putObject(req, RequestBody.empty())
            println("directory Created.. [${key}]")
        } catch (ex) {
            println("Could not create Directory..with - to Key/Path [${directoryToCreate}]" + StackTraceUtils.deepSanitize(ex))
        }
    }

    static void objectExists(String key) {

        S3Client s3Client = getAWSClient(accessKey, secretKey, bucketName)
        HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
        try {
            HeadObjectResponse response = s3Client.headObject(request)
            if (response.asBoolean()) {
                println("Path [${key}] exists")
            } else {
                println("Path [${key}] does not exists")
            }

        } catch (ex) {
            println("Path [${key}] is not present..")
        }
    }

    static S3Client getAWSClient(String accessKey, String secretKey, String bucketName) {
        S3Client s3Client = null
        try {
            return S3Client.builder()
                    .region(Region.US_EAST_2)
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                    .build()
        } catch (ex) {
            println("Could not connect to AWS S3 Bucket..[${bucketName}]" + StackTraceUtils.deepSanitize(ex))
        }

        return s3Client
    }

}
