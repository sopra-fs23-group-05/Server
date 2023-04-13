package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class UserGetDTO {

  private Long id;
  private String username;
  private boolean isLeader;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
