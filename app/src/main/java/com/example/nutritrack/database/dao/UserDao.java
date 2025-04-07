package com.example.nutritrack.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutritrack.database.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insert(UserEntity user);
    
    @Update
    void update(UserEntity user);
    
    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<UserEntity> getUserById(int userId);
    
    @Query("SELECT * FROM users WHERE username = :username")
    UserEntity getUserByUsername(String username);
    
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    UserEntity login(String username, String password);
    
    @Query("SELECT * FROM users")
    LiveData<List<UserEntity>> getAllUsers();
    
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
}