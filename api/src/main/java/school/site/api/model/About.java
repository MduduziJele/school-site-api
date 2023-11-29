package school.site.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class About {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aboutId;
    private String aboutustext;
    private String mission;
    private String vision;
    private String ImageUrlVision;
    private String ImageUrlMission;
    
    
    

    public String getImageUrlVision() {
        return ImageUrlVision;
    }

    public void setImageUrlVision(String imageUrlVision) {
        ImageUrlVision = imageUrlVision;
    }

    public String getImageUrlMission() {
        return ImageUrlMission;
    }

    public void setImageUrlMission(String imageUrlMission) {
        ImageUrlMission = imageUrlMission;
    }

    public About(Integer aboutId, String aboutustext, String mission, String vision) {
        this.aboutId = aboutId;
        this.aboutustext = aboutustext;
        this.mission = mission;
        this.vision = vision;
    }

    public About() {
    }

    /**
     * @return Integer return the aboutId
     */
    public Integer getaboutId() {
        return aboutId;
    }

    /**
     * @param aboutId the aboutId to set
     */
    public void setaboutId(Integer aboutId) {
        this.aboutId = aboutId;
    }

    /**
     * @return String return the aboutustext
     */
    public String getAboutustext() {
        return aboutustext;
    }

    /**
     * @param aboutustext the aboutustext to set
     */
    public void setAboutustext(String aboutustext) {
        this.aboutustext = aboutustext;
    }

    /**
     * @return String return the mission
     */
    public String getMission() {
        return mission;
    }

    /**
     * @param mission the mission to set
     */
    public void setMission(String mission) {
        this.mission = mission;
    }

    /**
     * @return String return the vision
     */
    public String getVision() {
        return vision;
    }

    /**
     * @param vision the vision to set
     */
    public void setVision(String vision) {
        this.vision = vision;
    }

}
