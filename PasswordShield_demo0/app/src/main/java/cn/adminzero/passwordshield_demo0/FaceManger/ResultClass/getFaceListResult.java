package cn.adminzero.passwordshield_demo0.FaceManger.ResultClass;

//log_id	是	uint64	请求标识码，随机数，唯一
//        face_list	是	array	人脸列表
//        + face_token	是	string	人脸图片的唯一标识
//        +ctime	是	string	人脸创建时间

import java.lang.reflect.Array;

public class getFaceListResult {
    private int log_id;
    private Array face_list;
    private String face_token;
    private String ctime;

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public Array getFace_list() {
        return face_list;
    }

    public void setFace_list(Array face_list) {
        this.face_list = face_list;
    }

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
