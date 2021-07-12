package edu.cnm.deepdive.codebreaker.model.dto;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Game {

  @Expose
  private String id;

  @Expose
  private Date created;

  @Expose
  private String pool;

  @Expose
  private int length;

  @Expose
  private int guessCount;

  @Expose
  private boolean solved;

  private final List<Guess> guesses = new LinkedList<>();


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getPool() {
    return pool;
  }

  public void setPool(String pool) {
    this.pool = pool;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getGuessCount() {
    return guessCount;
  }

  public void setGuessCount(int guessCount) {
    this.guessCount = guessCount;
  }

  public boolean isSolved() {
    return solved;
  }

  public void setSolved(boolean solved) {
    this.solved = solved;
  }

  public List<Guess> getGuesses() {
    return guesses;
  }

}


