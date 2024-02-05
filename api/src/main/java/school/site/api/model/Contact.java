package school.site.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer id;
    private String school_email;
    private String school_phone;
    private String school_address;
    private String fb_link;
    private String instagram_link;
    private String tiktok_link;

    public Contact() {
    }

    public Contact(Integer id, String school_email, String school_phone, String school_address, String fb_link, String instagram_link, String tiktok_link) {
        this.id = id;
        this.school_email = school_email;
        this.school_phone = school_phone;
        this.school_address = school_address;
        this.fb_link = fb_link;
        this.instagram_link = instagram_link;
        this.tiktok_link = tiktok_link;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchool_email() {
        return school_email;
    }

    public void setSchool_email(String school_email) {
        this.school_email = school_email;
    }

    public String getSchool_phone() {
        return school_phone;
    }

    public void setSchool_phone(String school_phone) {
        this.school_phone = school_phone;
    }

    public String getSchool_address() {
        return school_address;
    }

    public void setSchool_address(String school_address) {
        this.school_address = school_address;
    }

    public String getFb_link() {
        return fb_link;
    }

    public void setFb_link(String fb_link) {
        this.fb_link = fb_link;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public void setInstagram_link(String instagram_link) {
        this.instagram_link = instagram_link;
    }

    public String getTiktok_link() {
        return tiktok_link;
    }

    public void setTiktok_link(String tiktok_link) {
        this.tiktok_link = tiktok_link;
    }
}
