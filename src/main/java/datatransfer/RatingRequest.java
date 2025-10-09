package datatransfer;

public class RatingRequest {
    java.sql.Timestamp createdAt;
    String title;
    int stars;
    String comment;

    public RatingRequest(String title, int stars, String comment, java.sql.Timestamp createdAt ) {
        this.title = title;
        this.stars = stars;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getStars() {
        return stars;
    }
    public void setStars(int stars) {
        this.stars = stars;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }


}
