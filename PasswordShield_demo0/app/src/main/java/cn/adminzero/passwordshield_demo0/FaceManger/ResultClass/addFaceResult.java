package cn.adminzero.passwordshield_demo0.FaceManger.ResultClass;


//
//log_id	是	uint64	请求标识码，随机数，唯一
//        face_token	是	string	人脸图片的唯一标识
//        location	是	array	人脸在图片中的位置
//        +left	是	double	人脸区域离左边界的距离
//        +top	是	double	人脸区域离上边界的距离
//        +width	是	double	人脸区域的宽度
//        +height	是	double	人脸区域的高度
//        +rotation	是	int64	人脸框相对于竖直方向的顺时针旋转角，[-180,180]
public class addFaceResult {
    private int error_code;
    private String error_msg;
    private String log_id;
    private String timestamp;
    private int cached;

    private class result{

        private String face_token;
        private class location{
            private double left;
            private double top;
            private double width;
            private double height;
            private double rotation;

            public double getLeft() {
                return left;
            }

            public double getTop() {
                return top;
            }

            public double getWidth() {
                return width;
            }

            public double getHeight() {
                return height;
            }

            public double getRotation() {
                return rotation;
            }
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public String getFace_token() {
            return face_token;
        }
    }

    public int getError_code() {
        return error_code;
    }

    public String getError_Message() {
        return error_msg;
    }

    public String getLog_id() {
        return log_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getCached() {
        return cached;
    }
}
