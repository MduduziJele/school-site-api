package school.site.api.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @SequenceGenerator(name = "postSequence",
            sequenceName = "postSequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "postSequence")
    private Integer postId;
    private String postTitle;

    private String postContent;
    private String postImage;
    private Date createAt;
    private Date updateAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany()
    @JoinTable(
            name = "post_category",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> category;

    public Post() {
    }

    public Post(String postTitle, String postContent, String postImage, Date createAt, Date updateAt, User user, List<Category> category) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postImage = postImage;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.user = user;
        this.category = category;
    }

    public Post(Integer postId, String postTitle, String postContent, String postImage, Date createAt, Date updateAt, User user, List<Category> category) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postImage = postImage;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.user = user;
        this.category = category;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }
}
