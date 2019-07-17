package cn.adminzero.passwordshield_demo0.FaceManger.ResultClass;

import java.util.List;

public class matchResult_result {
    private double score;
    private List<Face_List> face_list;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<Face_List> getFace_list() {
        return face_list;
    }

    public void setFace_list(List<Face_List> face_list) {
        this.face_list = face_list;
    }
}
