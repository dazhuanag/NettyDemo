public class VersionInfo {

    /**
     * app名称
     */
    public  String appName;

    /**
     * APP版本
     */
    public  Double versionCode;

    /**
     * 强制更新
     */
    public  boolean updateForce;

    /**
     * 更新信息
     */
    public  String updateInfo;

    /**
     * app更新地址
     */
    public  String downloadUrl;

    public  String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }



    public boolean isUpdateForce() {
        return updateForce;
    }

    public void setUpdateForce(boolean updateForce) {
        this.updateForce = updateForce;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }


    public Double getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Double versionCode) {
        this.versionCode = versionCode;
    }

    public VersionInfo(String appName, Double versionCode, boolean updateForce, String updateInfo, String downloadUrl) {
        this.appName = appName;
        this.versionCode = versionCode;
        this.updateForce = updateForce;
        this.updateInfo = updateInfo;
        this.downloadUrl = downloadUrl;
    }
}
