package i.ddsweb.ttmm;

public class AmountInfo
{
    String personid;
    String personname;
    String pricee;
    String  personnum;


    public AmountInfo() {
    }

    public AmountInfo(String personid, String personname, String personnum, String pricee) {
        this.personname = personname;
        this.pricee = pricee;
        this.personid=personid;
        this.personnum=personnum;
    }

    public String getPersonnum() {
        return personnum;
    }

    public void setPersonnum(String personnum) {
        this.personnum = personnum;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPricee() {
        return pricee;
    }

    public void setPricee(String pricee) {
        this.pricee = pricee;
    }
}
