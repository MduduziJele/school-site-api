package school.site.api.model;

import jakarta.persistence.*;
import school.site.api.model.enums.CategoriesEnum;

import java.util.List;
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @SequenceGenerator(name = "categorySequence",
            sequenceName = "categorySequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "categorySequence")
    private Integer categoryId;
    @Enumerated(value =EnumType.STRING)
    private CategoriesEnum categories;
    @ManyToMany(mappedBy = "category")
    public List<Post> posts;

    public Category() {
    }

    public Category(Integer categoryId, CategoriesEnum categories, List<Post> posts) {
        this.categoryId = categoryId;
        this.categories = categories;
        this.posts = posts;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public CategoriesEnum getCategories() {
        return categories;
    }

    public void setCategories(CategoriesEnum categories) {
        this.categories = categories;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
