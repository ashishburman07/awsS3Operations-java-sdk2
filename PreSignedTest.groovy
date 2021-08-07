import org.apache.http.client.entity.UrlEncodedFormEntity

class PreSignedTest {

    public static void main(String[] args) {

        String key = "archive/input/file2.pdf"

        //SignedS3Service.getDownloadUrl(key)

        key = "archive/input/moved/pdf3_UploadURL.pdf"
        File file = new File("C:\\Users\\blusy\\Documents\\ashish\\aws\\pdfs\\file1_output.pdf")
        URL url = new URL(SignedS3Service.getUploadUrl(key))
        URLConnection con = url.openConnection()
        HttpURLConnection http = (HttpURLConnection) con
        http.setRequestMethod("POST")
        http.doOutput=true
        OutputStream outputStream = http.getOutputStream()
        outputStream.write(file.bytes)

        if (http.responseCode == 200) {
            println("File uploaded..")
        }


    }
}
