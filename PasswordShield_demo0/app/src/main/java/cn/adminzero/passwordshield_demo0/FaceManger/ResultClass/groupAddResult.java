package cn.adminzero.passwordshield_demo0.FaceManger.ResultClass;
//
//  "error_code": 0,
//          "log_id": 3314921889,
public class groupAddResult {
    private int error_code;
    private int log_id;
    private String Error_Msg;

    public String getError_Msg() {
        return Error_Msg;
    }

    public void setError_Msg(String error_Msg) {
        Error_Msg = error_Msg;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }
}
