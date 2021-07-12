package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "completed_game")
public class CompletedGame {

  @PrimaryKey
  @ColumnInfo(name = "completed_game_id")
  private long id;

  @ColumnInfo(name = "service_key")
  private String serviceKey;

  private Date started;

  @ColumnInfo(index = true)
  private Date completed;

  @ColumnInfo(index = true)
  private int attempts;

  @ColumnInfo(name = "code_length", index = true)
  private int codeLength;

  @ColumnInfo(name = "pool_size")
  private int poolSize;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public Date getStarted() {
    return started;
  }

  public void setStarted(Date started) {
    this.started = started;
  }

  public Date getCompleted() {
    return completed;
  }

  public void setCompleted(Date completed) {
    this.completed = completed;
  }

  public int getAttempts() {
    return attempts;
  }

  public void setAttempts(int attempts) {
    this.attempts = attempts;
  }

  public int getCodeLength() {
    return codeLength;
  }

  public void setCodeLength(int codeLength) {
    this.codeLength = codeLength;
  }

  public int getPoolSize() {
    return poolSize;
  }

  public void setPoolSize(int poolSize) {
    this.poolSize = poolSize;
  }
}
