package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class UserPostDTO {
    private Long id;

  private String username;

  private boolean isLeader;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
