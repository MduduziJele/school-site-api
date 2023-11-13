package school.site.api.payload.response;

import org.springframework.http.ResponseCookie;

import java.util.List;

public class UserInfoResponse {
    private Long id;

    private String first_name;
    private String last_name;
    private String email;
    private List<String> roles;

    private ResponseCookie jwtCookie;

    public UserInfoResponse(Long id, String first_name, String last_name, String email, List<String> roles, ResponseCookie jwtCookie) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.roles = roles;
        this.jwtCookie = jwtCookie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ResponseCookie getJwtCookie() {
        return jwtCookie;
    }

    public void setJwtCookie(ResponseCookie jwtCookie) {
        this.jwtCookie = jwtCookie;
    }

    public List<String> getRoles() {
        return roles;
    }
}