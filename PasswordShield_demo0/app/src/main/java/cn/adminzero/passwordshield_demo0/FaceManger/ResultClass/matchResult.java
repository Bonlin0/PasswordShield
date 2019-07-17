package cn.adminzero.passwordshield_demo0.FaceManger.ResultClass;

public class matchResult {
    private int error_code;
    private String error_msg;
    private String log_id;
    private String timestamp;
    private int cached;
    private matchResult_result result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getCached() {
        return cached;
    }

    public void setCached(int cached) {
        this.cached = cached;
    }

    public matchResult_result getResult() {
        return result;
    }

    public void setResult(matchResult_result result) {
        this.result = result;
    }
}
