import java.util.ArrayList;

public class Invoice {
    private String year,moth,day,hour,minutes,seconds;
    private String title, seller, buyer;
    private ArrayList<Car> list;

    public String generateNumber(){
        return "VAT/"+year+"/"+moth+"/"+day+"/"+hour+"/"+minutes;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMoth() {
        return moth;
    }

    public void setMoth(String moth) {
        this.moth = moth;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public ArrayList<Car> getList() {
        return list;
    }

    public void setList(ArrayList<Car> list) {
        this.list = list;
    }

    public Invoice(String year, String moth, String day, String hour, String minutes, String seconds, String title, String seller, String buyer, ArrayList<Car> list) {
        this.year = year;
        this.moth = moth;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
        this.title = title;
        this.seller = seller;
        this.buyer = buyer;
        this.list = list;
    }
}
