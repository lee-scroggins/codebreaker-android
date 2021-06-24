package edu.cnm.deepdive.codebreaker.model;

import com.google.gson.annotations.Expose;
import java.util.Date;

public class Error {

  @Expose
  private Date timestamp;

  @Expose
  private int status;

  @Expose
  private String message;

  @Expose
  private String error;

  @Expose
  private String path;

  @Expose
  private Details details;

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Details getDetails() {
    return details;
  }

  public void setDetails(Details details) {
    this.details = details;
  }

  public static class Details {

    @Expose
    private String text;

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

  }

}
