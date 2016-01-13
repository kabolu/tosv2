package edu.must.tos.bean;

public class StudReceipt {

	private String studentNo;
	
	private String chineseName;
	
	private String englishName;
	
	private String programName;
	
	private String majorName;
	
	private String orderIntake;
	
	private int orderSeqno;
	
	private Double amercemount;
	
	private String title;
	
	private String isbn;
	
	private Double mopNetPrice;
	
	private Double rmbNetPrice;
	
	private double bookPrice;
	
	private Double mopFuturePrice;
	
	private Double rmbFuturePrice;
	
	private Double mopWithPrice;
	
	private Double rmbWithPrice;
	
	private int confirmQty;
	
	private int withDrawQty;
	
	private int withDrawQty2;
	
	private Double prePaidMop;
	
	private Double prePaidRmb;
	
	private String paidCurrency;
	
	private String paidStatus;
	
	private double paidAmount;
	
	private String netPaidCurrency = null;
	
	private Double netPaidAmount = null;
	
	private Double curRate = null;

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getOrderIntake() {
		return orderIntake;
	}

	public void setOrderIntake(String orderIntake) {
		this.orderIntake = orderIntake;
	}

	public int getOrderSeqno() {
		return orderSeqno;
	}

	public void setOrderSeqno(int orderSeqno) {
		this.orderSeqno = orderSeqno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Double getMopNetPrice() {
		return mopNetPrice;
	}

	public void setMopNetPrice(Double mopNetPrice) {
		this.mopNetPrice = mopNetPrice;
	}

	public Double getRmbNetPrice() {
		return rmbNetPrice;
	}

	public void setRmbNetPrice(Double rmbNetPrice) {
		this.rmbNetPrice = rmbNetPrice;
	}

	public Double getMopWithPrice() {
		return mopWithPrice;
	}

	public void setMopWithPrice(Double mopWithPrice) {
		this.mopWithPrice = mopWithPrice;
	}

	public Double getRmbWithPrice() {
		return rmbWithPrice;
	}

	public void setRmbWithPrice(Double rmbWithPrice) {
		this.rmbWithPrice = rmbWithPrice;
	}

	public int getConfirmQty() {
		return confirmQty;
	}

	public void setConfirmQty(int confirmQty) {
		this.confirmQty = confirmQty;
	}

	public int getWithDrawQty() {
		return withDrawQty;
	}

	public void setWithDrawQty(int withDrawQty) {
		this.withDrawQty = withDrawQty;
	}

	public Double getPrePaidMop() {
		return prePaidMop;
	}

	public void setPrePaidMop(Double prePaidMop) {
		this.prePaidMop = prePaidMop;
	}

	public Double getPrePaidRmb() {
		return prePaidRmb;
	}

	public void setPrePaidRmb(Double prePaidRmb) {
		this.prePaidRmb = prePaidRmb;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Double getAmercemount() {
		return amercemount;
	}

	public void setAmercemount(Double amercemount) {
		this.amercemount = amercemount;
	}

	public Double getMopFuturePrice() {
		return mopFuturePrice;
	}

	public Double getRmbFuturePrice() {
		return rmbFuturePrice;
	}

	public void setMopFuturePrice(Double mopFuturePrice) {
		this.mopFuturePrice = mopFuturePrice;
	}

	public void setRmbFuturePrice(Double rmbFuturePrice) {
		this.rmbFuturePrice = rmbFuturePrice;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}

	public int getWithDrawQty2() {
		return withDrawQty2;
	}

	public void setWithDrawQty2(int withDrawQty2) {
		this.withDrawQty2 = withDrawQty2;
	}

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}

	public double getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(double bookPrice) {
		this.bookPrice = bookPrice;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public Double getCurRate() {
		return curRate;
	}

	public void setCurRate(Double curRate) {
		this.curRate = curRate;
	}

	public String getNetPaidCurrency() {
		return netPaidCurrency;
	}

	public void setNetPaidCurrency(String netPaidCurrency) {
		this.netPaidCurrency = netPaidCurrency;
	}

	public Double getNetPaidAmount() {
		return netPaidAmount;
	}

	public void setNetPaidAmount(Double netPaidAmount) {
		this.netPaidAmount = netPaidAmount;
	}

}
