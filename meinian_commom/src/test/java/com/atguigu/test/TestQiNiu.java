package com.atguigu.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

public class TestQiNiu {
    @Test
    public void testUpload() {
        //构造一个带指定 Region 对象的配置类
        // Configuration cfg = new Configuration(Region.region0());
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "NZTDb2uLswqcEhnxZef9iXEX1zrm0D4KjWCrP57M";
        String secretKey = "5Ub55CATxJnlcE2aWaAnnbq0JPwM4CJn75V7j4T3";
        String bucket = "yhd123";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "d:/temp/Snipaste_2021-01-08_22-14-40.png";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "xxx.png";//上传后文件名，不指定会自动生成文件名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @Test
            public void testDelete(){
//构造一个带指定 Region 对象的配置类
            Configuration cfg = new Configuration(Zone.zone2());
    //...其他参数参考类注释
        String accessKey = "NZTDb2uLswqcEhnxZef9iXEX1zrm0D4KjWCrP57M";
        String secretKey = "5Ub55CATxJnlcE2aWaAnnbq0JPwM4CJn75V7j4T3";
        String bucket = "yhd123";
    String key = "xxx.png";
    Auth auth = Auth.create(accessKey, secretKey);
    BucketManager bucketManager = new BucketManager(auth, cfg);
    try{
        bucketManager.delete(bucket, key);
    } catch(
    QiniuException ex)

    {
        //如果遇到异常，说明删除失败
        System.err.println(ex.code());
        System.err.println(ex.response.toString());
    }
}
}
