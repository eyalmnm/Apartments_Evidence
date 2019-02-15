package md.starlab.apartmentsevidenceapp.history.apartment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BuildingAssignedTransaction implements Parcelable {

    @SerializedName("title")
    private String title;
    @SerializedName("color")
    private String color;  // "#FFFFFF"
    @SerializedName("urgency")
    private String urgency;
    @SerializedName("end_at")
    private String end_at; // "2018-08-15T13:19:53.897Z"
    @SerializedName("is_transaction")
    private boolean hasQuestionnaire;
    @SerializedName("transaction_id")
    private String transactionId;

    public BuildingAssignedTransaction(String title, String color, String urgency, String end_at,
                                       boolean hasQuestionnaire, String transactionId) {
        this.title = title;
        this.color = color;
        this.urgency = urgency;
        this.end_at = end_at;
        this.hasQuestionnaire = hasQuestionnaire;
        this.transactionId = transactionId;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getEnd_at() {
        int tIdx = end_at.indexOf('T');
        if (-1 < tIdx) {
            return end_at.substring(0, tIdx);
        }
        return end_at;
    }

    public boolean isHasQuestionnaire() {
        return hasQuestionnaire;
    }

    public String getTransactionId() {
        return transactionId;
    }


    public static final Parcelable.Creator<BuildingAssignedTransaction> CREATOR = new Parcelable.Creator<BuildingAssignedTransaction>() {
        @Override
        public BuildingAssignedTransaction createFromParcel(Parcel source) {
            return new BuildingAssignedTransaction(source);
        }

        @Override
        public BuildingAssignedTransaction[] newArray(int size) {
            return new BuildingAssignedTransaction[size];
        }
    };

    protected BuildingAssignedTransaction(Parcel in) {
        this.title = in.readString();
        this.color = in.readString();
        this.urgency = in.readString();
        this.end_at = in.readString();
        this.hasQuestionnaire = in.readByte() != 0;
        this.transactionId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.color);
        dest.writeString(this.urgency);
        dest.writeString(this.end_at);
        dest.writeByte(this.hasQuestionnaire ? (byte) 1 : (byte) 0);
        dest.writeString(this.transactionId);
    }
}
