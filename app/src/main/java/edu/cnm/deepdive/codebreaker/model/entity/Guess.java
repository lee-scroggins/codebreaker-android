package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(
    indices = {
        @Index(value = {"servie_key"}, unique = true)
    },
    foreignKeys = {
        @ForeignKey( //specify entity class FK will reference
            entity = Game.class,
            childColumns = {"game_id"},
            parentColumns = {"game_id"},
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Guess {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "guess_id")
  private long id;

  @Expose
  @SerializedName("id")
  @ColumnInfo(name = "service_key")
  @NonNull
  private String serviceKey;

  @ColumnInfo(name = "game_id", index = true)
  private long gameId;

  @Expose
  @ColumnInfo(index = true)
  @NonNull
  private Date created;

  @Expose
  @ColumnInfo(name = "guess_text")
  @NonNull
  private String text;

  @Expose
  @ColumnInfo(name = "exact_matches")
  private int exactMatches;

  @Expose
  @ColumnInfo(name = "near_matches")
  private int nearMatches;

  @Expose
  private boolean solution;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(@NonNull String serviceKey) {
    this.serviceKey = serviceKey;
  }

  public long getGameId() {
    return gameId;
  }

  public void setGameId(long gameId) {
    this.gameId = gameId;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  @NonNull
  public String getText() {
    return text;
  }

  public void setText(@NonNull String text) {
    this.text = text;
  }

  public int getExactMatches() {
    return exactMatches;
  }

  public void setExactMatches(int exactMatches) {
    this.exactMatches = exactMatches;
  }

  public int getNearMatches() {
    return nearMatches;
  }

  public void setNearMatches(int nearMatches) {
    this.nearMatches = nearMatches;
  }

  public boolean isSolution() {
    return solution;
  }

  public void setSolution(boolean solution) {
    this.solution = solution;
  }
}
