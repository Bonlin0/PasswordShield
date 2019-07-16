package cn.adminzero.passwordshield_demo0.FaceManger.ResultClass;

import java.lang.reflect.Array;

//face_token	是	string	人脸标志
//        user_list	是	array	匹配的用户信息列表
//        +group_id	是	string	用户所属的group_id
//        +user_id	是	string	用户的user_id
//        +user_info	是	string	注册用户时携带的user_info
//        +score	是	float	用户的匹配得分，推荐阈值80分
public class SerachResult {
    private String face_token;
    private Array   user_list;
    private String group_id;
    private String user_id;
    private String user_info;
    private float score;

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public Array getUser_list() {
        return user_list;
    }

    public void setUser_list(Array user_list) {
        this.user_list = user_list;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
