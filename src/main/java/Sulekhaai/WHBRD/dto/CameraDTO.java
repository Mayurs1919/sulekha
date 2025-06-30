package Sulekhaai.WHBRD.dto;

public class CameraDTO {
    private String cameraId;
    private String deviceName;

    public CameraDTO(String cameraId, String deviceName) {
        this.cameraId = cameraId;
        this.deviceName = deviceName;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
