package edu.cnm.deepdive.codebreaker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Game {

  @Expose
  @PrimaryKey
  @ColumnInfo(name = "game_id")
  private String id;

  @Expose
  private Date created;

  @Expose
  private String pool;

  @Expose
  private int length;

  @Expose
  @ColumnInfo(name = "guess_count")
  private int guessCount;

  @Expose
  private boolean solved;

  @Ignore
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


