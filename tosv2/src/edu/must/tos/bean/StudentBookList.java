package edu.must.tos.bean;

public class StudentBookList{
    private String studentNo = null;
    private String isbn = null;
    private String bookTitle = null;
    private String author = null;
    private String publisher = null;
    private String publishYear = null;
    private String edition = null;
    private String language = null;
    private String bookType = null;
    private String remarks = null;
    private String courseCode = null;
    private String crsChineseName = null;
    private String crsEnglishName = null;
    private Integer confirmQty = null;
    private String courseType = null;
    private Boolean isEnrolled = false;
    private String price = null;
    private Double mopPrice = null;
    private Double netMopPrice = null;
    private Double rmbPrice = null;
    private String withdrawInd = null;
    private String paidStatus = null;
    private String supplement = null;


    public String getSupplement(){
        return supplement;
    }

    public void setSupplement(String supplement){
        this.supplement = supplement;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookType() {
        return bookType;
    }

    public String getCrsChineseName() {
        return crsChineseName;
    }

    public String getCrsEnglishName() {
        return crsEnglishName;
    }

    public String getEdition() {
        return edition;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getLanguage() {
        return language;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Integer getConfirmQty() {
        return confirmQty;
    }

    public String getCourseType() {
        return courseType;
    }

    public Double getMopPrice() {
        return mopPrice;
    }

    public Double getRmbPrice() {
        return rmbPrice;
    }

    public String getPrice() {
        return price;
    }

    public String getWithdrawInd() {
        return withdrawInd;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public Boolean getIsEnrolled() {
        return isEnrolled;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle.replaceAll("\\n|\\r"," ").replaceAll("'","");
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public void setCrsChineseName(String crsChineseName) {
        this.crsChineseName = crsChineseName;
    }

    public void setCrsEnglishName(String crsEnglishName) {
        this.crsEnglishName = crsEnglishName;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks.replaceAll("\\n|\\r"," ").replaceAll("'","");
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setConfirmQty(Integer confirmQty) {
        this.confirmQty = confirmQty;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public void setMopPrice(Double mopPrice) {
        this.mopPrice = mopPrice;
    }

    public void setRmbPrice(Double rmbPrice) {
        this.rmbPrice = rmbPrice;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setWithdrawInd(String withdrawInd) {
        this.withdrawInd = withdrawInd;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public void setIsEnrolled(Boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }

	public Double getNetMopPrice() {
		return netMopPrice;
	}

	public void setNetMopPrice(Double netMopPrice) {
		this.netMopPrice = netMopPrice;
	}
    
    
}