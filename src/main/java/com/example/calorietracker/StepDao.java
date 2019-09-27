package com.example.calorietracker;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StepDao {
    @Query("SELECT * FROM step")
    List<Step> getAll();
    @Query("SELECT * FROM step WHERE username = :username LIMIT 1")
    Step findByUsername(String username);

    @Query("SELECT * FROM step WHERE uid = :stepId LIMIT 1")
    Step findByUid(int stepId);

    @Insert
    void insertAll(Step... steps);

    @Insert
    long insert(Step step);

    @Insert
    void delete(Step step);

    @Update(onConflict = REPLACE)
    public void updateUsers(Step...steps);

    @Query("DELETE FROM step")
    void deleteALL();


}
