package md.starlab.apartmentsevidenceapp.login.model;

import com.google.gson.annotations.SerializedName;

public class LoginServerResponse {

    @SerializedName("is_admin")
    private boolean isAdmin;
    @SerializedName("is_staff")
    private boolean isStaff;
    @SerializedName("token")
    private String token;

    public LoginServerResponse(boolean isAdmin, boolean isStaff, String token) {
        this.isAdmin = isAdmin;
        this.isStaff = isStaff;
        this.token = token;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public String getToken() {
        return token;
    }
}
