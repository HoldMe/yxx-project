package com.yxx.framework.util.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2021/1/27
 */
public class AwsS3UploadUtil {


    /**
     * access_key_id 你的亚马逊S3服务器访问密钥ID
     */
//    @Value("ACCESS_KEY")
    private static final String ACCESS_KEY = "minioadmin";
    /**
     * secret_key 你的亚马逊S3服务器访问密钥
     */
//    @Value("SECRET_KEY")
    private static final String SECRET_KEY = "minioadmin";

    /**
     * end_point 你的亚马逊S3服务器连接路径和端口(新版本不再需要这个,直接在创建S3对象的时候根据桶名和Region自动获取)
     *
     * 格式: https://桶名.s3-你的Region名称.amazonaws.com
     * 示例: https://xxton.s3-cn-north-1.amazonaws.com
     */
    /**
     * bucketname 你的亚马逊S3服务器创建的桶名
     */
//    @Value("BUCKET_NAME")
    private static final String BUCKET_NAME = "edifier";

    /**
     * 创建访问凭证对象
     */
    private static final BasicAWSCredentials basicAws = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

    /**
     * 创建s3对象
     */
    private static final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAws))
            // 设置服务器所属地区
            .withRegion(Regions.CN_NORTH_1)
            .build();

    public static String upload(File file, String uploadPath){
        try {
            if (file == null) {
                return null;
            }
            //设置文件目录
            if(StringUtils.isNotEmpty(uploadPath)){
                uploadPath= "/".equals(uploadPath.substring(uploadPath.length()-1))?uploadPath:uploadPath+"/";
            }else{
                uploadPath="default/";
            }
            //生成随机文件名
            String expandedName= file.getName().substring(file.getName().lastIndexOf("."));
            String key = uploadPath + UUID.randomUUID().toString() +expandedName;
            // 设置文件上传对象
            PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, key, file);
            // 设置公共读取
            request.withCannedAcl(CannedAccessControlList.PublicRead);
            // 上传文件
            PutObjectResult putObjectResult = amazonS3.putObject(request);
            if (null != putObjectResult) {
                return key;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String downloadFile(String key){
        try {
            if(StringUtils.isEmpty(key)){
                return null;
            }
            GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key);
            return amazonS3.generatePresignedUrl(httpRequest).toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 客户端上传
     * @param filePath
     */
    public static void uploadFile(String filePath){
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("https://play.min.io", "Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("asiatrip");
            if(!isExist) {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("asiatrip");
            }

            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject("asiatrip","moonlanding.png", filePath);
        } catch(Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public static void main(String[] args) {
        File file = new File("C:/Users/lenovo/Desktop/moonlanding.png");
        System.out.println(AwsS3UploadUtil.upload(file, "/"));
//        AwsS3UploadUtil.uploadFile("C:/Users/lenovo/Desktop/moonlanding.png");
    }
}
