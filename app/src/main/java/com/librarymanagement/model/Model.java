package com.librarymanagement.model;

public class Model {

    // for user info
    private String id;
    private String name;
    private String phone;
    private String address;
    private String school;
    private String studentId;
    private String categoryImage;
    private String category;
    private String profilepic;

    // for book info
    private String bookName;
    private String bookLocation;
    private String booksCount;
    private String imageUrl;
    private String pushKey;

    // for notification
    private String notification;

    public Model() {
    }

    public Model(String id, String name, String phone, String address, String school, String studentId, String categoryImage, String category, String profilepic, String bookName, String bookLocation, String booksCount, String imageUrl, String pushKey, String notification) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.school = school;
        this.studentId = studentId;
        this.categoryImage = categoryImage;
        this.category = category;
        this.profilepic = profilepic;
        this.bookName = bookName;
        this.bookLocation = bookLocation;
        this.booksCount = booksCount;
        this.imageUrl = imageUrl;
        this.pushKey = pushKey;
        this.notification = notification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }

    public String getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(String booksCount) {
        this.booksCount = booksCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
