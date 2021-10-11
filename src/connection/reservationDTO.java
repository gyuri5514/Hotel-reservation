package connection;


/*
 DTO = DB와 연동할 DTO 클래스 생성  
 DB에 있는 변수명과 동일하게 생성 
 접근자 메서드, 생성자 2가지, tostring메서드 생성 
 */

public class reservationDTO {
	private String rname;
	private String name;
	private String pw;
	private String phone;
	private String startday;
	private String endday;
	private int people;
	private int pay;
	
	public reservationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public reservationDTO(String rname, String name, String pw, String phone, String startday, String endday,
			int people, int pay) {
		super();
		this.rname = rname;
		this.name = name;
		this.pw = pw;
		this.phone = phone;
		this.startday = startday;
		this.endday = endday;
		this.people = people;
		this.pay = pay;
	}
	
	public reservationDTO(String rname, String phone, int people) {
		this.rname = rname;
		this.phone = phone;
		this.people = people;
	}
	
	
	public reservationDTO(String startday) {
		this.startday = startday;
	}
	
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStartday() {
		return startday;
	}
	public void setStartday(String startday) {
		this.startday = startday;
	}
	public String getEndday() {
		return endday;
	}
	public void setEndday(String endday) {
		this.endday = endday;
	}
	public int getPeople() {
		return people;
	}
	public void setPeople(int people) {
		this.people = people;
	}
	public int getPay() {
		return pay;
	}
	public void setPay(int pay) {
		this.pay = pay;
	}
	@Override
	public String toString() {
		return "reservationDTO [rname=" + rname + ", name=" + name + ", pw=" + pw + ", phone=" + phone + ", startday="
				+ startday + ", endday=" + endday + ", people=" + people + ", pay=" + pay + "]";
	}
	
}
