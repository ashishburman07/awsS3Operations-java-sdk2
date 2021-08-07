class TestClass {


    public static void main(String[] args) {

        S3Operations s3Operations = new S3Operations()

        //inputs
        String AWSPath = "archive/input/"
        String localFilePath = "C:\\Users\\blusy\\Documents\\ashish\\aws\\pdfs\\file1.pdf"
        File file = new File(localFilePath)
        String key = AWSPath + file.name
        //1) Pushing file in to S3 Bucket
       // s3Operations.uploadObject(file, key)

        //2) Download file
        localFilePath = "C:\\Users\\blusy\\Documents\\ashish\\aws\\pdfs\\file1_output.pdf"
        //s3Operations.downloadObject(key, localFilePath)  // 'archive/input/file2.pdf'

        //3) get list of objects
        //s3Operations.getListObject("archive/input/file")

        //4) Move Object
        String fromKey=key
        String toKey=AWSPath+"moved/"+file.name
       // s3Operations.moveObject(fromKey,toKey)

        //5) create Directory
        //s3Operations.createDirectory("archive/output/moved/")

        //6) delete object
        //S3Operations.deleteObject("archive/input/CUS_PART1.csv")

        //S3Operations.objectExists("archive/input/file2.pdf")


    }
}
